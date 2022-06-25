package com.aliyuncs.fc.model;

import com.google.gson.annotations.SerializedName;



public class TLSConfig {

    @SerializedName("cipherSuites")
    public java.util.List<String> cipherSuites;

    @SerializedName("minVersion")
    public String minVersion;

    @SerializedName("maxVersion")
    public String maxVersion;

    public TLSConfig (String minVersion, java.util.List<String> cipherSuites) {
        this.cipherSuites = cipherSuites;
        this.minVersion = minVersion;
    }

    public java.util.List<String> getCipherSuites() {
        return cipherSuites;
    }

    public TLSConfig setCipherSuites(java.util.List<String> cipherSuites) {
        this.cipherSuites = cipherSuites;
        return this;
    }

    public String getMinVersion() {
        return minVersion;
    }

    public TLSConfig setMinVersion(String minVersion) {
        this.minVersion = minVersion;
        return this;
    }

    public TLSConfig setMaxVersion(String maxVersion) {
        this.maxVersion = maxVersion;
        return this;
    }
}
