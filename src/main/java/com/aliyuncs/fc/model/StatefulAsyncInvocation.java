package com.aliyuncs.fc.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * TODO: add javadoc
 */

@Data
public class StatefulAsyncInvocation {

    public StatefulAsyncInvocation() {}

    @SerializedName("serviceName")
    public String serviceName;

    @SerializedName("functionName")
    public String functionName;

    @SerializedName("qualifier")
    public String qualifier;

    @SerializedName("invocationId")
    public String invocationId;

    @SerializedName("status")
    public String status;

    @SerializedName("startedTime")
    public String startedTime;

    @SerializedName("endTime")
    public String endTime;

    @SerializedName("invocationErrorMessage")
    public String invocationErrorMessage;

    @SerializedName("invocationPayload")
    public String invocationPayload;

    @SerializedName("destinationStatus")
    public String destinationStatus;

    @SerializedName("requestId")
    public String requestId;

    @SerializedName("alreadyRetriedTimes")
    public String alreadyRetriedTimes;

    public String getServiceName() {
        return serviceName;
    }

    public String getFunctionName() {
        return functionName;
    }

    public String getQualifier() {
        return qualifier;
    }

    public String getInvocationId() {
        return invocationId;
    }

    public String getStatus() {
        return status;
    }

    public String getStartedTime() {
        return startedTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getInvocationErrorMessage() {
        return invocationErrorMessage;
    }

    public String getInvocationPayload() {
        return invocationPayload;
    }

    public String getDestinationStatus() { return destinationStatus; }

    public String getAlreadyRetriedTimess() { return alreadyRetriedTimes; }

    public String getRequestId() { return requestId; }


    public StatefulAsyncInvocation setServiceName(String serviceName) {
        this.serviceName = serviceName;
        return this;
    }

    public StatefulAsyncInvocation setFunctionName(String functionName) {
        this.functionName = functionName;
        return this;
    }

    public StatefulAsyncInvocation setQualifier(String qualifier) {
        this.qualifier = qualifier;
        return this;
    }

    public StatefulAsyncInvocation setInvocationId(String invocationId) {
        this.invocationId = invocationId;
        return this;
    }

    public StatefulAsyncInvocation setStatus(String status) {
        this.status = status;
        return this;
    }

    public StatefulAsyncInvocation setStartedTime(String startedTime) {
        this.startedTime = startedTime;
        return this;
    }

    public StatefulAsyncInvocation setEndTime(String endTime) {
        this.endTime = endTime;
        return this;
    }

    public StatefulAsyncInvocation setInvocationErrorMessage(String invocationErrorMessage) {
        this.invocationErrorMessage = invocationErrorMessage;
        return this;
    }

    public StatefulAsyncInvocation setInvocationPayload(String invocationPayload) {
        this.invocationPayload = invocationPayload;
        return this;
    }

    public StatefulAsyncInvocation setDestinationStatus(String destinationStatus) {
        this.destinationStatus = destinationStatus;
        return this;
    }

    public StatefulAsyncInvocation setAlreadyRetriedTimes(String alreadyRetriedTimes) {
        this.alreadyRetriedTimes = alreadyRetriedTimes;
        return this;
    }

    public StatefulAsyncInvocation setRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }
}