package com.aliyuncs.fc.model;

import com.google.gson.annotations.SerializedName;

/**
 * OSS Trigger config
 */
public class OSSTriggerConfig {

    @SerializedName("events")
    private String[] events;

    @SerializedName("filter")
    private OSSTriggerFilter filter;

    public OSSTriggerConfig(String[] events, String filterPrefix, String filterSuffix) {
        this.events = events;
        this.filter = new OSSTriggerFilter(filterPrefix, filterSuffix);
    }

    public String[] getEvents() {
        return events;
    }

    public OSSTriggerFilter getFilter() {
        return filter;
    }
}
