package com.aliyuncs.fc.model;

public class ReservedCapacityMetaData {
    private String instanceId;
    private Integer cu;
    private String deadLine;
    private String createdTime;
    private String lastModifiedTime;

    public ReservedCapacityMetaData(String instanceId, Integer cu, String deadLine, String createdTime, String lastModifiedTime) {
        this.instanceId = instanceId;
        this.cu = cu;
        this.deadLine = deadLine;
        this.createdTime = createdTime;
        this.lastModifiedTime = lastModifiedTime;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public Integer getCU() {
        return cu;
    }

    public String getDeadLine() {
        return deadLine;
    }

    public String getCreateTime() {
        return createdTime;
    }

    public String getLastModifiedTime() {
        return lastModifiedTime;
    }
}