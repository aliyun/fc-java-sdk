package com.aliyuncs.fc.model;

import com.google.gson.annotations.SerializedName;

public class HttpTriggerConfig {

    @SerializedName("authType")
    private final HttpAuthType authType;

    @SerializedName("methods")
    private final HttpMethod[] methods;

    public HttpTriggerConfig(HttpAuthType authType, HttpMethod[] methods) {
        this.authType = authType;
        this.methods = methods;
    }

    public HttpAuthType getAuthType() {
        return authType;
    }

    public HttpMethod[] getMethods() {
        return methods;
    }
}
