package com.aliyuncs.fc.model;

import com.google.gson.annotations.SerializedName;

public class CustomRuntimeConfig {
    @SerializedName("args")
    private String[] args;

    @SerializedName("command")
    private String[] command;

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

    public String[] getCommand() {
        return command;
    }

    public void setCommand(String[] command) {
        this.command = command;
    }
}
