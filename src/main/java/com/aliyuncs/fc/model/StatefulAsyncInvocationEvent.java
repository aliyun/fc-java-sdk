package com.aliyuncs.fc.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * TODO: add javadoc
 */

@Data
public class StatefulAsyncInvocationEvent {

    public StatefulAsyncInvocationEvent(){}

    @SerializedName("eventId")
    public long eventId;

    @SerializedName("status")
    public String status;

    @SerializedName("timestamp")
    public long timestamp;

    @SerializedName("eventDetail")
    public String eventDetail;

    public StatefulAsyncInvocationEvent setEventId(long eventId) {
        this.eventId = eventId;
        return this;
    }
    public StatefulAsyncInvocationEvent setStatus(String status) {
        this.status = status;
        return this;
    }
    public StatefulAsyncInvocationEvent setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }
    public StatefulAsyncInvocationEvent setEventDetail(String eventDetail) {
        this.eventDetail = eventDetail;
        return this;
    }

    public long getEventId() {
        return eventId;
    }

    public String getStatus() {
        return status;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getEventDetail() {
        return eventDetail;
    }

}