package com.aliyuncs.fc.model;

import com.google.gson.annotations.SerializedName;

/**
 * TODO: add javadoc
 */
public class FunctionCodeMetadata {

    @SerializedName("url")
    private String url;

    @SerializedName("checksum")
    private String checksum;

    public FunctionCodeMetadata(String url, String checksum) {
        this.url = url;
        this.checksum = checksum;
    }

    public String getUrl() {
        return url;
    }

    public String getChecksum() {
        return checksum;
    }
}
