package com.aliyuncs.fc.model;

import com.google.gson.annotations.SerializedName;

/**
 * Time Trigger config
 */
public class TimeTriggerConfig {

    @SerializedName("cronExpression")
    private String cronExpression;

    @SerializedName("payload")
    private TimeTriggerPayload payload;

    @SerializedName("enable")
    private boolean enable;

    public TimeTriggerConfig(String cronExpression, TimeTriggerPayload payload, boolean enable) {
        this.cronExpression = cronExpression;
        this.payload = payload;
        this.enable = enable;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public TimeTriggerPayload getPayload() {
        return payload;
    }

    public void setPayload(TimeTriggerPayload payload) {
        this.payload = payload;
    }
}
