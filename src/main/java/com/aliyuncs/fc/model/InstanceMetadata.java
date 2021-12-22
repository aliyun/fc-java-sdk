package com.aliyuncs.fc.model;

import com.google.gson.annotations.SerializedName;
import java.util.Map;

/**
 * TODO: add javadoc
 */
public class InstanceMetadata {

    @SerializedName("instanceId")
    private String instanceID;
    
    @SerializedName("versionId")
    private String versionID;

    public InstanceMetadata() {
        this.instanceID = "";
    }

    public InstanceMetadata(String instanceID) {
        this.instanceID = instanceID;
    }

    public String getInstanceID() {
        return instanceID;
    }

    public void setInstanceID(String instanceId) {
        this.instanceID = instanceId;
    }


    public String getVersionID() {
        return versionID;
    }

    public void setVersionID(String versionID) {
        this.versionID = versionID;
    }
}
