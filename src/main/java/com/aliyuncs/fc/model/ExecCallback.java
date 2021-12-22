package com.aliyuncs.fc.model;

import org.java_websocket.handshake.ServerHandshake;

public interface ExecCallback {
    public void onOpen(ServerHandshake handshakedata);

    public void onClose(int code, String reason, boolean remote);

    public void onError(Exception ex);

    public void onStdout(String message);

    public void onStderr(String message);
}
