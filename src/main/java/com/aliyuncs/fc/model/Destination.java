package com.aliyuncs.fc.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * TODO: add javadoc
 */
@Data
public class Destination {

    @SerializedName("destination")
    public String destination;

    public Destination setDestination(String destination) {
        this.destination = destination;
        return this;
    }

    public String getDestination() {
        return destination;
    }
}
