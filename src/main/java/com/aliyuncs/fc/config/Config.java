package com.aliyuncs.fc.config;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * TODO: add javadoc
 */
public class Config {

    private static final String ENDPOINT_FMT = "%s.%s.fc.aliyuncs.com";
    private static final String PROPERTIES_FILE = "fc.properties";
    private String endpoint;
    private String apiVersion = "2016-08-15";
    private String accessKeyID;
    private String accessKeySecret;
    private String securityToken;
    private Boolean isDebug = false;
    private String accountId;
    private String uid;
    private int connectTimeoutMillis = 60000;
    private int readTimeoutMillis = 60000;

    private String host;
    private String userAgent;

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
        this.userAgent = "";
        try {
            Properties props = new Properties();
            InputStream input = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE);
            props.load(input);
            if (props.get("useragent.version") != null ) {
                this.userAgent = "java-sdk-" + props.get("useragent.version");
            }
        } catch (IOException e) {
            throw new RuntimeException("Properties file " + PROPERTIES_FILE + " is not found");
        }
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

    public int getConnectTimeoutMillis() {
        return connectTimeoutMillis;
    }

    /**
     * Sets a specified timeout value, in milliseconds, to be used when opening a communications
     * link to the resource referenced by this URLConnection. If the timeout expires before the
     * connection can be established, a java.net.SocketTimeoutException is raised.
     * A timeout of zero is interpreted as an infinite timeout.
     *
     * Also see http://docs.oracle.com/javase/6/docs/api/java/net/URLConnection.html#setReadTimeout%28int%29
     * @return
     */
    public Config setConnectTimeoutMillis(int connectTimeoutMillis) {
        this.connectTimeoutMillis = connectTimeoutMillis;
        return this;
    }

    public int getReadTimeoutMillis() {
        return readTimeoutMillis;
    }

    /**
     * Sets the read timeout to a specified timeout, in milliseconds.
     * A non-zero value specifies the timeout when reading from Input stream when a connection
     * is established to a resource. If the timeout expires before there is data available for read,
     * a java.net.SocketTimeoutException is raised.
     * A timeout of zero is interpreted as an infinite timeout.
     *
     * Also see http://docs.oracle.com/javase/6/docs/api/java/net/URLConnection.html#setReadTimeout%28int%29
     * @param readTimeoutMillis
     * @return
     */
    public Config setReadTimeoutMillis(int readTimeoutMillis) {
        this.readTimeoutMillis = readTimeoutMillis;
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
