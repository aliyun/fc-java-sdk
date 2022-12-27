package com.aliyuncs.fc.model;

public class PathConfig {
    private String path;
    private String accountId;
    private String serviceName;
    private String functionName;
    private String qualifier;

    public PathConfig(String path, String serviceName, String functionName, String qualifier) {
        this.path = path;
        this.serviceName = serviceName;
        this.functionName = functionName;
        this.qualifier = qualifier;
    }

    public String getPath() {
        return this.path;
    }

    public PathConfig setPath(String path) {
        this.path = path;
        return this;
    }

    public String getServiceName() {
        return this.serviceName;
    }

    public PathConfig setServiceName(String serviceName) {
        this.serviceName = serviceName;
        return this;
    }

    public String getFunctionName() {
        return this.functionName;
    }

    public PathConfig setFunctionName(String functionName) {
        this.functionName = functionName;
        return this;
    }

    public String getQualifier() {
        return this.qualifier;
    }

    public PathConfig setQualifier(String qualifier) {
        this.qualifier = qualifier;
        return this;
    }

    public String getAccountId() {
        return this.accountId;
    }

    public PathConfig setAccountId(String accountId) {
        this.accountId = accountId;
        return this;
    }
}
