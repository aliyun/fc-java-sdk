package com.aliyuncs.fc.model;

import com.aliyuncs.fc.utils.Base64Helper;
import com.aliyuncs.fc.utils.ZipUtils;
import com.google.gson.annotations.SerializedName;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * TODO: add javadoc
 */
public class CustomContainerConfig {

    @SerializedName("image")
    public String image;

    @SerializedName("command")
    public String command;

    @SerializedName("args")
    public String args;

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
}
