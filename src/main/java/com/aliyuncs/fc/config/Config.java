package com.aliyuncs.fc.config;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

/**
 * TODO: add javadoc
 */
public class Config {

    private static final String ENDPOINT_FMT = "%s.fc.%s.aliyuncs.com";
    private String endpoint;
    private String apiVersion = "2016-08-15";
    private String accessKeyID;
    private String accessKeySecret;
    private String securityToken;
    private String userAgent = "java-sdk-1.0.0";
    private Boolean isDebug = false;
    private String accountId;
    private String uid;
    private int timeout = 60;
    private String host;

    public Config(String region, String uid, String accessKeyID, String accessKeySecret,
        String securityToken, boolean isHttps) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(region),"Region cannot be blank");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(uid),"Account ID cannot be blank");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(accessKeyID),"Access key cannot be blank");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(accessKeySecret),"Secret key cannot be blank");

        this.endpoint = buildEndpoint(region, uid, isHttps);
        this.accessKeyID = accessKeyID;
        this.accessKeySecret = accessKeySecret;
        this.uid = uid;
        this.securityToken = securityToken;
    }

    public String getUid() {
        return uid;
    }

    public Config setUid(String uid) {
        this.uid = uid;
        return this;
    }

    public String getAccountId() {
        return accountId;
    }

    public Config setAccoutId(String accountId) {
        this.accountId = accountId;
        return this;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public Config setEndpoint(String endpoint) {
        this.endpoint = endpoint;
        return this;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public Config setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
        return this;
    }

    public String getAccessKeyID() {
        return accessKeyID;
    }

    public Config setAccessKeyID(String accessKeyID) {
        this.accessKeyID = accessKeyID;
        return this;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public Config setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
        return this;
    }

    public Boolean getDebug() {
        return isDebug;
    }

    public Config setDebug(Boolean debug) {
        this.isDebug = debug;
        return this;
    }

    public int getTimeout() {
        return timeout;
    }

    public Config setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public String getHost() {
        return host;
    }

    public Config setHost(String host) {
        this.host = host;
        return this;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public Config setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    public String getSecurityToken() {
        return securityToken;
    }

    public Config setSecurityToken(String securityToken) {
        this.securityToken = securityToken;
        return this;
    }

    private String buildEndpoint(String region, String uid, boolean isHttps) {
        String endpoint = String.format(ENDPOINT_FMT, uid, region);
            String protocol = isHttps ? "https" : "http";
            return protocol + "://" + endpoint;
    }
}
