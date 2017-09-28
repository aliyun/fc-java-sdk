package com.aliyuncs.fc.model.trigger.log;

import com.google.gson.annotations.SerializedName;

/**
 * TODO: add javadoc
 */
public class SourceConfig {

    @SerializedName("logstore")
    private String logstore;

    public SourceConfig(String logstore) {
        this.logstore = logstore;
    }

    public String getLogstore() {
        return logstore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SourceConfig that = (SourceConfig) o;

        return logstore != null ? logstore.equals(that.logstore) : that.logstore == null;
    }

    @Override
    public int hashCode() {
        return logstore != null ? logstore.hashCode() : 0;
    }
}
