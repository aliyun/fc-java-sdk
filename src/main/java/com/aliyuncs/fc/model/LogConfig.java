package com.aliyuncs.fc.model;

/**
 * TODO: add javadoc
 */
public class LogConfig {

    private String project;
    private String logstore;
    private Boolean enableRequestMetrics;
    private Boolean enableInstanceMetrics;

    public LogConfig(String project, String logStore) {
        this.project = project;
        this.logstore = logStore;
    }

    public LogConfig(String project, String logStore, Boolean enableRequestMetrics) {
        this.project = project;
        this.logstore = logStore;
        this.enableRequestMetrics = enableRequestMetrics;
    }

    public LogConfig(String project, String logStore, Boolean enableRequestMetrics, Boolean enableInstanceMetrics) {
        this.project = project;
        this.logstore = logStore;
        this.enableRequestMetrics = enableRequestMetrics;
        this.enableInstanceMetrics = enableInstanceMetrics;
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

    public LogConfig setEnableInstanceMetrics(Boolean enableInstanceMetrics) {
        this.enableInstanceMetrics = enableInstanceMetrics;
        return this;
    }

    public Boolean getEnableInstanceMetrics() {
        return enableInstanceMetrics;
    }
}
