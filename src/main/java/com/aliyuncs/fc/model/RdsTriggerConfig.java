package com.aliyuncs.fc.model;

public class RdsTriggerConfig {

    private String[] subscriptionObjects;

    private Integer retry;

    private Integer concurrency;

    private String eventFormat;

    public RdsTriggerConfig(String[] subscriptionObjects, Integer retry, Integer concurrency, String eventFormat) {
        this.subscriptionObjects = subscriptionObjects;
        this.retry = retry;
        this.concurrency = concurrency;
        this.eventFormat = eventFormat;
    }

    public Integer getConcurrency() {
        return concurrency;
    }

    public Integer getRetry() {
        return retry;
    }

    public String getEventFormat() {
        return eventFormat;
    }

    public void setConcurrency(Integer concurrency) {
        this.concurrency = concurrency;
    }

    public void setEventFormat(String eventFormat) {
        this.eventFormat = eventFormat;
    }

    public void setRetry(Integer retry) {
        this.retry = retry;
    }

    public void setSubscriptionObjects(String[] subscriptionObjects) {
        this.subscriptionObjects = subscriptionObjects;
    }

    public String[] getSubscriptionObjects() {
        return subscriptionObjects;
    }
}
