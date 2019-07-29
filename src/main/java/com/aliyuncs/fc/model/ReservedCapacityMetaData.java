package com.aliyuncs.fc.model;

public class ReservedCapacityMetaData {
    private String instanceId;
    private Integer cu;
    private String deadline;
    private String createdTime;
    private String lastModifiedTime;

    public ReservedCapacityMetaData(String instanceId, Integer cu, String deadline, String createdTime, String lastModifiedTime) {
        this.instanceId = instanceId;
        this.cu = cu;
        this.deadline = deadline;
        this.createdTime = createdTime;
        this.lastModifiedTime = lastModifiedTime;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public Integer getCU() {
        return cu;
    }

    public String getDeadline() {
        return deadline;
    }

    public String getCreateTime() {
        return createdTime;
    }

    public String getLastModifiedTime() {
        return lastModifiedTime;
    }
}