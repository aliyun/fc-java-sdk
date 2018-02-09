package com.aliyuncs.fc.model;

import com.google.gson.annotations.SerializedName;

/**
 * Time Trigger config payload
 */
public class TimeTriggerPayload {
    @SerializedName("triggerTime")
    private String triggerTime;

    @SerializedName("triggerName")
    private String triggerName;

    @SerializedName("payload")
    private String payload;

    public TimeTriggerPayload(String triggerTime, String triggerName, String payload) {
        this.triggerTime = triggerTime;
        this.triggerName = triggerName;
        this.payload = payload;
    }

    public String getTriggerTime() {
        return triggerTime;
    }

    public void setTriggerTime(String triggerTime) {
        this.triggerTime = triggerTime;
    }

    public String getTriggerName() {
        return triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    public String isPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
