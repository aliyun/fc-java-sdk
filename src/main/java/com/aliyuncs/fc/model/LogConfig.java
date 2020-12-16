package com.aliyuncs.fc.model;

/**
 * TODO: add javadoc
 */
public class LogConfig {

    private String project;
    private String logstore;
    private Boolean enableRequestMetrics;

    public LogConfig(String project, String logStore, Boolean enableRequestMetrics) {
        this.project = project;
        this.logstore = logStore;
        this.enableRequestMetrics = enableRequestMetrics;
    }

    public LogConfig setProject(String project) {
        this.project = project;
        return this;
    }

    public String getProject() {
        return project;
    }

    public LogConfig setLogStore(String logStore) {
        this.logstore = logStore;
        return this;
    }

    public String getLogStore() {
        return logstore;
    }

    public LogConfig setEnableRequestMetrics(Boolean enableRequestMetrics) {
        this.enableRequestMetrics = enableRequestMetrics;
        return this;
    }

    public Boolean getEnableRequestMetrics() {
        return enableRequestMetrics;
    }
}
