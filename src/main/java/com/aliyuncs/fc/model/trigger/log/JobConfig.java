package com.aliyuncs.fc.model.trigger.log;

import com.google.gson.annotations.SerializedName;

/**
 * TODO: add javadoc
 */
public class JobConfig {

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
