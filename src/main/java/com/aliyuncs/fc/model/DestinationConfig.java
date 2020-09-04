package com.aliyuncs.fc.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * TODO: add javadoc
 */

@Data
public class DestinationConfig {

    public DestinationConfig(){}

    @SerializedName("onSuccess")
    public Destination onSuccess;

    @SerializedName("onFailure")
    public Destination onFailure;

    public DestinationConfig setOnSuccess(Destination onSuccess) {
        this.onSuccess = onSuccess;
        return this;
    }

    public Destination getOnSuccess() {
        return onSuccess;
    }

    public Destination getOnFailure() {
        return onFailure;
    }

    public DestinationConfig setonFailure(Destination onFailure) {
        this.onFailure = onFailure;
        return this;
    }

}