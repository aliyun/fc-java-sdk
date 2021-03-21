package com.aliyuncs.fc.model;

/**
 * Created by tw108174 on 18/10/30.
 */
public class AccountSettings {

    private String[] availableAZs;

    private String[] albEndpoints;

    public String[] getAvailableAZs() {
        return availableAZs;
    }

    public String[] getAlbEndpoints() {
        return albEndpoints;
    }
}
