package com.aliyuncs.fc.request;

import com.aliyuncs.fc.constants.Const;
import com.aliyuncs.fc.model.HttpAuthType;
import com.aliyuncs.fc.model.HttpMethod;
import com.google.common.net.UrlEscapers;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static com.aliyuncs.fc.auth.AcsURLEncoder.decode;
import static java.lang.String.format;

/**
 * used for http invocation
 */
public class HttpInvokeFunctionRequest extends InvokeFunctionRequest {

    private final String path;

    private final HttpMethod method;

    private final HttpAuthType authType;

    private final Map<String, String> queryParams = new HashMap<String, String>();

    public HttpInvokeFunctionRequest(String serviceName, String functionName, HttpAuthType authType, HttpMethod method) {
        this(serviceName, functionName, authType, method, "");
    }

    public HttpInvokeFunctionRequest(String serviceName, String functionName, HttpAuthType authType, HttpMethod method, String path) {
        super(serviceName, functionName);

        this.setInvocationType(Const.INVOCATION_TYPE_HTTP);

        try {
            // parse path. Path may contain parameters
            if (path != null) {
                URIBuilder uriBuilder = new URIBuilder(path);
                path = decode(uriBuilder.getPath());

                for (NameValuePair pair : uriBuilder.getQueryParams()) {
                    addQuery(decode(pair.getName()), decode(pair.getValue()));
                }
            }

            this.path = path == null ? "" : (path.startsWith("/") ? path.substring(1) : path);

        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }

        this.authType = authType;
        this.method = method;
    }

    public void addQuery(String name, String value) {
        if (value == null) value = "";
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

    public HttpMethod getMethod() {
        return method;
    }

    public HttpAuthType getAuthType() {
        return authType;
    }
}
