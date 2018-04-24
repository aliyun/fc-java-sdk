package com.aliyuncs.fc.request;

import com.aliyuncs.fc.constants.Const;
import com.aliyuncs.fc.model.HttpAuthType;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


import static java.lang.String.format;

/**
 * used for http invocation
 */
public class HttpInvokeFunctionRequest extends InvokeFunctionRequest {

    private final String path;

    private final String method;

    private final HttpAuthType authType;

    private final Map<String, String> queryParams = new TreeMap<String, String>();

    public HttpInvokeFunctionRequest(String serviceName, String functionName, HttpAuthType authType, String method) {
        this(serviceName, functionName, authType, method, "");
    }

    public HttpInvokeFunctionRequest(String serviceName, String functionName, HttpAuthType authType, String method, String path) {
        super(serviceName, functionName);

        this.setInvocationType(Const.INVOCATION_TYPE_HTTP);

        try {
            // parse path, path may contain parameters
            URIBuilder uriBuilder = new URIBuilder(path);
            path = uriBuilder.getPath();
            this.path = path == null ? "" : (path.startsWith("/") ? path.substring(1) : path);

            for (NameValuePair pair : uriBuilder.getQueryParams()) {
                addQuery(pair.getName(), pair.getValue());
            }
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }

        this.authType = authType;
        this.method = method;
    }

    public void addQuery(String name, String value) {
        this.queryParams.put(name, value);
    }

    @Override
    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    @Override
    public String getPath() {
        return format(Const.HTTP_INVOKE_FUNCTION_PATH, Const.API_VERSION, getServiceName(), getFunctionName(), path);
    }

    public String getMethod() {
        return method;
    }

    public HttpAuthType getAuthType() {
        return authType;
    }
}
