package com.aliyuncs.fc.model;

import java.util.Map;

/**
 * TODO: add javadoc
 */
public class PrepareUrl {

    private String url = null;
    private Map<String, String> header = null;

    public PrepareUrl(String url, Map<String, String> header) {
        this.url = url;
        this.header = header;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, String> getHeader() {
        return header;
    }
}
