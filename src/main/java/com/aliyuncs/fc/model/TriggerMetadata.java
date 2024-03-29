package com.aliyuncs.fc.model;

import com.google.gson.annotations.SerializedName;

/**
 * TODO: add javadoc
 */
public class TriggerMetadata {

    @SerializedName("triggerName")
    private String triggerName;

    @SerializedName("sourceArn")
    private String sourceArn;

    @SerializedName("triggerType")
    private String triggerType;

    @SerializedName("invocationRole")
    private String invocationRole;

    @SerializedName("createdTime")
    private String createdTime;

    @SerializedName("lastModifiedTime")
    private String lastModifiedTime;

    @SerializedName("qualifier")
    private String qualifier;

    @SerializedName("triggerConfig")
    private Object triggerConfig;

    @SerializedName("urlInternet")
    private String urlInternet;

    @SerializedName("urlIntranet")
    private String urlIntranet;

    public TriggerMetadata(String triggerName, String sourceArn, String triggerType, String invocationRole, String createdTime, String lastModifiedTime, String qualifier, Object triggerConfig, String urlInternet, String urlIntranet) {
        this.triggerName = triggerName;
        this.sourceArn = sourceArn;
        this.triggerType = triggerType;
        this.invocationRole = invocationRole;
        this.createdTime = createdTime;
        this.lastModifiedTime = lastModifiedTime;
        this.qualifier = qualifier;
        this.triggerConfig = triggerConfig;
        this.urlInternet = urlInternet;
        this.urlIntranet = urlIntranet;
    }

    public String getTriggerName() {
        return triggerName;
    }

    public String getSourceArn() {
        return sourceArn;
    }

    public String getTriggerType() {
        return triggerType;
    }

    public String getInvocationRole() {
        return invocationRole;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public String getLastModifiedTime() {
        return lastModifiedTime;
    }

    public String getQualifier() {
        return qualifier;
    }

    public Object getTriggerConfig() {
        return triggerConfig;
    }

    public String getUrlInternet() {
        return urlInternet;
    }

    public String getUrlIntranet() {
        return urlIntranet;
    }
}
