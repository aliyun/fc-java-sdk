package com.aliyuncs.fc.model;

import java.util.List;
import java.util.Map;

public class CdnEventsTriggerConfig {

    private String eventName;

    private String eventVersion;

    private String notes;

    private Map<String, List<String>> filter;

    public CdnEventsTriggerConfig() {

    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventVersion() {
        return eventVersion;
    }

    public void setEventVersion(String eventVersion) {
        this.eventVersion = eventVersion;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Map<String, List<String>> getFilter() {
        return filter;
    }

    public void setFilter(Map<String, List<String>> filter) {
        this.filter = filter;
    }
}
