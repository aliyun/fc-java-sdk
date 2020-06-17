package com.aliyuncs.fc.model;

import com.google.gson.annotations.SerializedName;
import java.util.Map;

/**
 * TODO: add javadoc
 */
public class FunctionMetadata {

    @SerializedName("functionId")
    private String functionId;

    @SerializedName("functionName")
    private String functionName;

    @SerializedName("description")
    private String description;

    @SerializedName("runtime")
    private String runtime;

    @SerializedName("handler")
    private String handler;

    @SerializedName("initializer")
    private String initializer;

    @SerializedName("timeout")
    private Integer timeout;

    @SerializedName("initializationTimeout")
    private Integer initializationTimeout;

    @SerializedName("memorySize")
    private Integer memorySize;

    @SerializedName("codeSize")
    private Integer codeSize;

    @SerializedName("codeChecksum")
    private String codeChecksum;

    @SerializedName("createdTime")
    private String createdTime;

    @SerializedName("lastModifiedTime")
    private String lastModifiedTime;

    @SerializedName("environmentVariables")
    private Map<String, String> environmentVariables;

    @SerializedName("instanceConcurrency")
    private Integer instanceConcurrency;

    @SerializedName("instanceType")
    private String instanceType;

    public FunctionMetadata(String functionId, String functionName, String description,
        String runtime, String handler, String initializer, Integer timeout, Integer initializationTimeout, Integer memorySize,
        int codeSize, String codeChecksum, String createdTime, String lastModifiedTime, String instanceType, Map<String, String> environmentVariables) {
        this.functionId = functionId;
        this.functionName = functionName;
        this.description = description;
        this.runtime = runtime;
        this.handler = handler;
        this.initializer = initializer;
        this.timeout = timeout;
        this.initializationTimeout = initializationTimeout;
        this.memorySize = memorySize;
        this.codeSize = codeSize;
        this.codeChecksum = codeChecksum;
        this.createdTime = createdTime;
        this.lastModifiedTime = lastModifiedTime;
        this.environmentVariables = environmentVariables;
        this.instanceType = instanceType;
    }
    public String getFunctionName() {
        return functionName;
    }

    public String getFunctionId() {
        return functionId;
    }

    public String getDescription() {
        return description;
    }

    public String getRuntime() {
        return runtime;
    }

    public String getHandler() {
        return handler;
    }

    public String getInitializer() {
        return initializer;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public Integer getInitializationTimeout() {
        return initializationTimeout;
    }

    public Integer getMemorySize() {
        return memorySize;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public String getLastModifiedTime() {
        return lastModifiedTime;
    }

    public Integer getCodeSize() {
        return codeSize;
    }

    public String getCodeChecksum() {
        return codeChecksum;
    }

    public Map<String, String> getEnvironmentVariables() {
        return environmentVariables;
    }

    public Integer getInstanceConcurrency() {
        return instanceConcurrency;
    }

    public String getInstanceType() {
        return instanceType;
    }
}
