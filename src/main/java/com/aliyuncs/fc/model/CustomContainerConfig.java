package com.aliyuncs.fc.model;

import com.google.gson.annotations.SerializedName;

/**
 * TODO: add javadoc
 */
public class CustomContainerConfig {

    public final static String AccelerationTypeDefault = "Default";
    public final static String AccelerationTypeNone = "None";

    @SerializedName("image")
    public String image;

    @SerializedName("command")
    public String command;

    @SerializedName("args")
    public String args;

    @SerializedName("accelerationType")
    public String accelerationType;

    @SerializedName("instanceID")
    public String instanceID;

    public CustomContainerConfig setImage(String image) {
        this.image = image;
        return this;
    }

    public String getImage() {
        return image;
    }

    public CustomContainerConfig setCommand(String command) {
        this.command = command;
        return this;
    }

    public String getCommand() {
        return command;
    }

    public CustomContainerConfig setArgs(String args) {
        this.args = args;
        return this;
    }

    public String getArgs() {
        return args;
    }

    public CustomContainerConfig setAccelerationType(String accelerationType) {
        this.accelerationType = accelerationType;
        return this;
    }

    public String getAccelerationType() {
        return accelerationType;
    }

    public CustomContainerConfig setInstanceID(String instanceID) {
        this.instanceID = instanceID;
        return this;
    }

    public String getInstanceID() {
        return instanceID;
    }

}
