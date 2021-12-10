package com.aliyuncs.fc.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class AccelerationInfo {

    public AccelerationInfo(){}

    @SerializedName("status")
    public String status;

    public AccelerationInfo setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getStatus() {
        return status;
    }
}
