package com.aliyuncs.fc.model.trigger.log;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * TODO: add javadoc
 */
public class TriggerConfig {

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

    public TriggerConfig() {
    }

    public SourceConfig getSourceConfig() {
        return sourceConfig;
    }

    public TriggerConfig setSourceConfig(SourceConfig sourceConfig) {
        this.sourceConfig = sourceConfig;
        return this;
    }

    public JobConfig getJobConfig() {
        return jobConfig;
    }

    public TriggerConfig setJobConfig(JobConfig jobConfig) {
        this.jobConfig = jobConfig;
        return this;
    }

    public LogConfig getLogConfig() {
        return logConfig;
    }

    public TriggerConfig setLogConfig(LogConfig logConfig) {
        this.logConfig = logConfig;
        return this;
    }

    public Map<String, Object> getFunctionParameter() {
        return functionParameter;
    }

    public TriggerConfig setFunctionParameter(Map<String, Object> functionParameter) {
        this.functionParameter = functionParameter;
        return this;
    }

    public Boolean isEnable() {
        return enable;
    }

    public TriggerConfig setEnable(Boolean enable) {
        this.enable = enable;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TriggerConfig that = (TriggerConfig) o;

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
}