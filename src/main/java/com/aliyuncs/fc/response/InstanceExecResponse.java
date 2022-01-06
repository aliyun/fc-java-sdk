package com.aliyuncs.fc.response;

import com.aliyuncs.fc.model.ExecWebsocket;
import com.aliyuncs.fc.model.ExecCallback;

public class InstanceExecResponse {
    private final ExecWebsocket execWebsocket;

    public InstanceExecResponse(ExecWebsocket ws) {
        execWebsocket = ws;
        execWebsocket.connect();
    }

    public ExecWebsocket getExecWebsocket() {
        return execWebsocket;
    }

    public void setCallback(ExecCallback callback) {
        execWebsocket.setCallback(callback);
    }

    public void send(String message) {
        execWebsocket.send(message);
    }

    public void send(byte[] message) {
        execWebsocket.send(message);
    }

    public void close() {
        execWebsocket.close();
    }

    public void close(int code, String reason) {
        execWebsocket.close(code, reason);
    }

    public void close(int code) {
        execWebsocket.close(code);
    }
}
