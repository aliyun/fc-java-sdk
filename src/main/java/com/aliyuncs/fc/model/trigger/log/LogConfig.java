package com.aliyuncs.fc.model.trigger.log;

import com.google.gson.annotations.SerializedName;

/**
 * TODO: add javadoc
 */
public class LogConfig {

    @SerializedName("project")
    private String project;

    @SerializedName("logstore")
    private String logstore;

    public LogConfig(String project, String logstore) {
        this.project = project;
        this.logstore = logstore;
    }

    public String getProject() {
        return project;
    }

    public String getLogstore() {
        return logstore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LogConfig logConfig = (LogConfig) o;

        if (project != null ? !project.equals(logConfig.project) : logConfig.project != null) return false;
        return logstore != null ? logstore.equals(logConfig.logstore) : logConfig.logstore == null;
    }

    @Override
    public int hashCode() {
        int result = project != null ? project.hashCode() : 0;
        result = 31 * result + (logstore != null ? logstore.hashCode() : 0);
        return result;
    }
}
