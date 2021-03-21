package com.aliyuncs.fc.model;

public class ALBSettings {

    private String accountId;

    private String[] endpoints;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String[] getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(String[] endpoints) {
        this.endpoints = endpoints;
    }
}
