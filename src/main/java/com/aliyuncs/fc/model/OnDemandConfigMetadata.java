package com.aliyuncs.fc.model;

import com.google.gson.annotations.SerializedName;

/**
 * TODO: add javadoc
 */
public class OnDemandConfigMetadata {

    @SerializedName("resource")
    private String resource;

    @SerializedName("maximumInstanceCount")
    private int maximumInstanceCount;


    public OnDemandConfigMetadata(String resource, Integer maximumInstanceCount) {
        this.resource = resource;
        this.maximumInstanceCount = maximumInstanceCount;
    }

    public String getResource() {
        return resource;
    }

    public int getMaximumInstanceCount() {
        return maximumInstanceCount;
    }
}
