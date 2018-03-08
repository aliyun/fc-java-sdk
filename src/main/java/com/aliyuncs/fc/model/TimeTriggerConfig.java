package com.aliyuncs.fc.model;

import com.google.gson.annotations.SerializedName;

/**
 * Time Trigger config
 */
public class TimeTriggerConfig {

    @SerializedName("cronExpression")
    private String cronExpression;

    @SerializedName("payload")
    private String payload;

    @SerializedName("enable")
    private Boolean enable;


    public TimeTriggerConfig() {
    }

    public TimeTriggerConfig(String cronExpression, String payload, boolean enable) {
        this.cronExpression = cronExpression;
        this.payload = payload;
        this.enable = enable;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public TimeTriggerConfig setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
        return this;
    }

    public Boolean isEnable() {
        return enable;
    }

    public TimeTriggerConfig setEnable(Boolean enable) {
        this.enable = enable;
        return this;
    }

    public String getPayload() {
        return payload;
    }

    public TimeTriggerConfig setPayload(String payload) {
        this.payload = payload;
        return this;
    }
}
