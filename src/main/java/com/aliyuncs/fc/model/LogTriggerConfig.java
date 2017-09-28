package com.aliyuncs.fc.model;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * TODO: add javadoc
 */
public class LogTriggerConfig {

    @SerializedName("sourceConfig")
    private SourceConfig sourceConfig;

    @SerializedName("jobConfig")
    private JobConfig jobConfig;

    @SerializedName("logConfig")
    private LogConfig logConfig;

    @SerializedName("functionParameter")
    private Map<String, Object> functionParameter;

    @SerializedName("enable")
    private Boolean enable;

    public LogTriggerConfig() {
    }

    public SourceConfig getSourceConfig() {
        return sourceConfig;
    }

    public LogTriggerConfig setSourceConfig(SourceConfig sourceConfig) {
        this.sourceConfig = sourceConfig;
        return this;
    }

    public JobConfig getJobConfig() {
        return jobConfig;
    }

    public LogTriggerConfig setJobConfig(JobConfig jobConfig) {
        this.jobConfig = jobConfig;
        return this;
    }

    public LogConfig getLogConfig() {
        return logConfig;
    }

    public LogTriggerConfig setLogConfig(LogConfig logConfig) {
        this.logConfig = logConfig;
        return this;
    }

    public Map<String, Object> getFunctionParameter() {
        return functionParameter;
    }

    public LogTriggerConfig setFunctionParameter(Map<String, Object> functionParameter) {
        this.functionParameter = functionParameter;
        return this;
    }

    public Boolean isEnable() {
        return enable;
    }

    public LogTriggerConfig setEnable(Boolean enable) {
        this.enable = enable;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LogTriggerConfig that = (LogTriggerConfig) o;

        if (enable != that.enable) return false;
        if (sourceConfig != null ? !sourceConfig.equals(that.sourceConfig) : that.sourceConfig != null) return false;
        if (jobConfig != null ? !jobConfig.equals(that.jobConfig) : that.jobConfig != null) return false;
        if (logConfig != null ? !logConfig.equals(that.logConfig) : that.logConfig != null) return false;
        return functionParameter != null ? functionParameter.equals(that.functionParameter) : that.functionParameter == null;
    }

    @Override
    public int hashCode() {
        int result = sourceConfig != null ? sourceConfig.hashCode() : 0;
        result = 31 * result + (jobConfig != null ? jobConfig.hashCode() : 0);
        result = 31 * result + (logConfig != null ? logConfig.hashCode() : 0);
        result = 31 * result + (functionParameter != null ? functionParameter.hashCode() : 0);
        result = 31 * result + (enable ? 1 : 0);
        return result;
    }

    /**
     * TODO: add javadoc
     */
    public static class SourceConfig {

        @SerializedName("logstore")
        private String logstore;

        public SourceConfig(String logstore) {
            this.logstore = logstore;
        }

        public String getLogstore() {
            return logstore;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            SourceConfig that = (SourceConfig) o;

            return logstore != null ? logstore.equals(that.logstore) : that.logstore == null;
        }

        @Override
        public int hashCode() {
            return logstore != null ? logstore.hashCode() : 0;
        }
    }


    /**
     * TODO: add javadoc
     */
    public static class JobConfig {

        @SerializedName("maxRetryTime")
        private Integer maxRetryTime;

        @SerializedName("triggerInterval")
        private Integer triggerInterval;

        public JobConfig() {
        }

        public Integer getMaxRetryTime() {
            return maxRetryTime;
        }

        public JobConfig setMaxRetryTime(Integer maxRetryTime) {
            this.maxRetryTime = maxRetryTime;
            return this;
        }

        public Integer getTriggerInterval() {
            return triggerInterval;
        }

        public JobConfig setTriggerInterval(Integer triggerInterval) {
            this.triggerInterval = triggerInterval;
            return this;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            JobConfig jobConfig = (JobConfig) o;

            if (maxRetryTime != null ? !maxRetryTime.equals(jobConfig.maxRetryTime) : jobConfig.maxRetryTime != null)
                return false;
            return triggerInterval != null ? triggerInterval.equals(jobConfig.triggerInterval) : jobConfig.triggerInterval == null;
        }

        @Override
        public int hashCode() {
            int result = maxRetryTime != null ? maxRetryTime.hashCode() : 0;
            result = 31 * result + (triggerInterval != null ? triggerInterval.hashCode() : 0);
            return result;
        }
    }

    /**
     * TODO: add javadoc
     */
    public static class LogConfig {

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
}