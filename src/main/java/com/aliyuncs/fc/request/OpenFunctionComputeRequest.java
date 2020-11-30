package com.aliyuncs.fc.request;

import com.aliyuncs.fc.exceptions.ClientException;
import com.aliyuncs.fc.http.HttpRequest;
import com.google.common.base.Strings;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Map;

public class OpenFunctionComputeRequest extends HttpRequest {

    private String action;

    public OpenFunctionComputeRequest() {
        super();
    }

    @Override
    public String getPath() {
        return null;
    }

    @Override
    public void validate() throws ClientException {
        if (Strings.isNullOrEmpty(action)) {
            throw new ClientException("action cannot be blank");
        }
        if (!StringUtils.equals(action, "OpenFCService")) {
            throw new ClientException("action value must be OpenFCService");
        }
    }

    @Override
    public HttpURLConnection getHttpConnection(String urls, String method) throws IOException {
        return super.getHttpConnection(urls, method);
    }

    @Override
    public void setHeader(String key, String value) {
        super.setHeader(key, value);
    }

    @Override
    public byte[] getPayload() {
        return super.getPayload();
    }

    @Override
    public Map<String, String> getQueryParams() {
        return super.getQueryParams();
    }

    @Override
    public Map<String, String> getHeaders() {
        return super.getHeaders();
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
