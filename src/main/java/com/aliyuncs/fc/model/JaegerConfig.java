package com.aliyuncs.fc.model;

public class JaegerConfig {
    private String endpoint;

    public JaegerConfig(){}

    public JaegerConfig(String endpoint) {
        this.endpoint = endpoint;
    }

    public JaegerConfig setEndpoint(String endpoint) {
        this.endpoint = endpoint;
        return this;
    }

    public String getEndpoint() {
        return endpoint;
    }
}
