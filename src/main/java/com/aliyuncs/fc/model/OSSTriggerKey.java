package com.aliyuncs.fc.model;

import com.google.gson.annotations.SerializedName;

/**
 * Used to bind OSS Trigger Key
 */
public class OSSTriggerKey {

    @SerializedName("prefix")
    private String prefix;

    @SerializedName("suffix")
    private String suffix;

    public OSSTriggerKey(String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }
}

