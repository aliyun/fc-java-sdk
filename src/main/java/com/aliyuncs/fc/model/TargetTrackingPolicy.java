package com.aliyuncs.fc.model;

import com.google.gson.annotations.SerializedName;

public class TargetTrackingPolicy {

    @SerializedName("name")
    private String name;

    @SerializedName("startTime")
    private String startTime;

    @SerializedName("endTime")
    private String endTime;

    @SerializedName("metricType")
    private String metricType;

    @SerializedName("metricTarget")
    private Double metricTarget;

    @SerializedName("minCapacity")
    private Integer minCapacity;

    @SerializedName("maxCapacity")
    private Integer maxCapacity;

    public TargetTrackingPolicy(String name, String startTime, String endTime, String metricType, Double metricTarget, Integer minCapacity, Integer maxCapacity) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.metricType = metricType;
        this.metricTarget = metricTarget;
        this.minCapacity = minCapacity;
        this.maxCapacity = maxCapacity;
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

    public String getMetricType() {
        return metricType;
    }

    public void setMetricType(String metricType) {
        this.metricType = metricType;
    }

    public Double getMetricTarget() {
        return metricTarget;
    }

    public void setMetricTarget(Double metricTarget) {
        this.metricTarget = metricTarget;
    }

    public Integer getMinCapacity() {
        return minCapacity;
    }

    public void setMinCapacity(Integer minCapacity) {
        this.minCapacity = minCapacity;
    }

    public Integer getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(Integer maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    @Override
    public String toString() {
        return "TargetTrackingPolicy{" +
                "name='" + name + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", metricType='" + metricType + '\'' +
                ", metricTarget=" + metricTarget +
                ", minCapacity=" + minCapacity +
                ", maxCapacity=" + maxCapacity +
                '}';
    }
}
