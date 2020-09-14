package com.aliyuncs.fc.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * TODO: add javadoc
 */

@Data
public class AsyncConfig {

    public AsyncConfig() {}

    public AsyncConfig (String serviceName, String qualifier, String functionName) {
        this.service = serviceName;
        this.qualifier = qualifier;
        this.function = functionName;
    }
    @SerializedName("service")
    public String service;

    @SerializedName("function")
    public String function;

    @SerializedName("qualifier")
    public String qualifier;

    @SerializedName("lastModifiedTime")
    public String lastModifiedTime;

    @SerializedName("createdTime")
    public String createdTime;

    @SerializedName("destinationConfig")
    public DestinationConfig destinationConfig;

    @SerializedName("maxAsyncEventAgeInSeconds")
    public Integer maxAsyncEventAgeInSeconds;

    @SerializedName("maxAsyncRetryAttempts")
    public Integer maxAsyncRetryAttempts;

    public String getService() {
        return service;
    }

    public String getFunction() {
        return function;
    }

    public String getQualifier() {
        return qualifier;
    }

    public String getLastModifiedTime() {
        return lastModifiedTime;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public DestinationConfig getDestinationConfig() {
        return destinationConfig;
    }

    public Integer getMaxAsyncEventAgeInSeconds() {
        return maxAsyncEventAgeInSeconds;
    }

    public Integer getMaxAsyncRetryAttempts() {
        return maxAsyncRetryAttempts;
    }

    public AsyncConfig setService(String service) {
        this.service = service;
        return this;
    }

    public AsyncConfig setQualifier(String qualifier) {
        this.function = function;
        return this;
    }

    public AsyncConfig setFunction(String function) {
        this.function = function;
        return this;
    }

    public AsyncConfig setLastModifiedTime(String lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
        return this;
    }

    public AsyncConfig setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
        return this;
    }

    public AsyncConfig setDestinationConfig(DestinationConfig destinationConfig) {
        this.destinationConfig = destinationConfig;
        return this;
    }

    public AsyncConfig setMaxAsyncEventAgeInSeconds(Integer maxAsyncEventAgeInSeconds) {
        this.maxAsyncEventAgeInSeconds = maxAsyncEventAgeInSeconds;
        return this;
    }

    public AsyncConfig setMaxAsyncRetryAttempts(Integer maxAsyncRetryAttempts) {
        this.maxAsyncRetryAttempts = maxAsyncRetryAttempts;
        return this;
    }
}