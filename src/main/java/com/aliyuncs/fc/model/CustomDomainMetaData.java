package com.aliyuncs.fc.model;

public class CustomDomainMetaData {
    private String domainName;
    private String accountId;
    private String protocol;
    private String apiVersion;
    private RouteConfig routeConfig;
    private String createdTime;
    private String lastModifiedTime;

    public CustomDomainMetaData(String domainName, String accountId, String protocol, String apiVersion, RouteConfig routeConfig, String createdTime, String lastModifiedTime) {
        this.domainName = domainName;
        this.accountId = accountId;
        this.protocol = protocol;
        this.apiVersion = apiVersion;
        this.routeConfig = routeConfig;
        this.createdTime = createdTime;
        this.lastModifiedTime = lastModifiedTime;
    }

    public String getDomainName() {
        return domainName;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public RouteConfig getRouteConfig() {
        return routeConfig;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public String getLastModifiedTime() {
        return lastModifiedTime;
    }
}