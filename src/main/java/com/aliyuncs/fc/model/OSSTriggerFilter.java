package com.aliyuncs.fc.model;

import com.google.gson.annotations.SerializedName;

/**
 * OSS event notification filter
 */
public class OSSTriggerFilter {

    @SerializedName("key")
    private OSSTriggerKey key;

    public OSSTriggerFilter(String filterPrefix, String filterSuffix) {
        this.key = new OSSTriggerKey(filterPrefix, filterSuffix);
    }

    public void setKey(OSSTriggerKey key) {
        this.key = key;
    }

    public OSSTriggerKey getKey() {
        return key;
    }
}
