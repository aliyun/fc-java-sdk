package com.aliyuncs.fc.model;

import com.google.gson.annotations.SerializedName;

public class ScheduledAction {

    @SerializedName("name")
    private String name;

    @SerializedName("startTime")
    private String startTime;

    @SerializedName("endTime")
    private String endTime;

    @SerializedName("target")
    private Integer target;

    @SerializedName("scheduleExpression")
    private String scheduleExpression;

    public ScheduledAction(String name, String startTime, String endTime, Integer target, String scheduleExpression) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.target = target;
        this.scheduleExpression = scheduleExpression;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getTarget() {
        return target;
    }

    public void setTarget(Integer target) {
        this.target = target;
    }

    public String getScheduleExpression() {
        return scheduleExpression;
    }

    public void setScheduleExpression(String scheduleExpression) {
        this.scheduleExpression = scheduleExpression;
    }
}
