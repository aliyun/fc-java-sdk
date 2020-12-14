package com.aliyuncs.fc.model;

import com.google.gson.annotations.SerializedName;

public class LifecycleHook {
    @SerializedName("handler")
    private String handler;

    @SerializedName("timeout")
    private Integer timeout;

    public LifecycleHook() {}

    public LifecycleHook(String handler, Integer timeout) {
        this.handler = handler;
        this.timeout = timeout;
    }

    public String getHandler() {
        return handler;
    }

    public Integer getTimeout() {
        return timeout;
    }
}