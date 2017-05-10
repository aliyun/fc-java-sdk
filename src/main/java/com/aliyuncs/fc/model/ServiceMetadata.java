package com.aliyuncs.fc.model;

/**
 * TODO: add javadoc
 */
public class ServiceMetadata {

    private String serviceName;
    private String description;
    private String role;
    private LogConfig logConfig;
    private String serviceId;
    private String createdTime;
    private String lastModifiedTime;

    public ServiceMetadata(String serviceName, String description, String role,
        LogConfig logConfig, String serviceId, String createdTime, String lastModifiedTime) {
        this.serviceName = serviceName;
        this.description = description;
        this.role = role;
        this.logConfig = logConfig;
        this.serviceId = serviceId;
        this.createdTime = createdTime;
        this.lastModifiedTime = lastModifiedTime;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getDescription() {
        return description;
    }

    public String getRole() {
        return role;
    }

    public LogConfig getLogConfig() {
        return logConfig;
    }

    public String getServiceId() {
        return serviceId;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public String getLastModifiedTime() {
        return lastModifiedTime;
    }

}
