package com.aliyuncs.fc.model;

/**
 * TODO: add javadoc
 */
public class LogConfig {

    private String project;
    private String logstore;

    public LogConfig(String project, String logStore) {
        this.project = project;
        this.logstore = logStore;
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
}
