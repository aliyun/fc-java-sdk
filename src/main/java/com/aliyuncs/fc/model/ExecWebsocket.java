package com.aliyuncs.fc.model;

import java.net.URI;
import java.util.Map;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class ExecWebsocket extends WebSocketClient {
    static final byte STDIN = 0;
    static final byte STDOUT = 1;
    static final byte STDERR = 2;
    static final byte SYSERR = 3;

    private ExecCallback callback = null;

    public ExecWebsocket(URI url, Map<String, String> httpHeaders) {
        super(url, httpHeaders);
    }

    public void setCallback(ExecCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        if (callback != null) {
            callback.onOpen(handshakedata);
        } else {
            System.out.println("onOpen");
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        if (callback != null) {
            callback.onClose(code, reason, remote);
        } else {
            System.out.printf("CLOSE: %d %s\n", code, reason);
        }
    }

    @Override
    public void onError(Exception ex) {
        if (callback != null) {
            callback.onError(ex);
        } else {
            System.out.printf("ERROR:%s\n", ex.getMessage());
        }
    }

    @Override
    public void onMessage(String message) {
        switch (message.charAt(0)) {
        case STDOUT:
            onStdout(message.substring(1));
            break;
        case STDERR:
            onStderr(message.substring(1));
            break;
        case SYSERR:
            Exception e = new Exception(message.substring(1));
            onError(e);
            break;
        default:
            throw new RuntimeException("Unknown message type: " + message);
        }
    }

    @Override
    public void send(String message) {
        super.send(  message.getBytes());
    }

    @Override
    public void send(byte[] message) {
        byte[] buf = new byte[message.length + 1];
        buf[0] = STDIN;
        for (int i = 0; i < message.length; i++) {
            buf[i+1] = message[i];
        }
        super.send(buf);
    }

    public void onStdout(String message) {
        if (callback != null) {
            callback.onStdout(message);
        } else {
            System.out.printf("STDOUT:%s\n", message);
        }

    }

    public void onStderr(String message) {
        if (callback != null) {
            callback.onStderr(message);
        } else {
            System.out.printf("STDERR:%s\n", message);
        }
    }
}
