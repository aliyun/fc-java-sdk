package com.aliyuncs.fc.auth;

import com.aliyuncs.fc.model.HttpMethod;
import com.aliyuncs.fc.utils.FcUtil;
import org.apache.http.client.utils.URIBuilder;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Strings.isNullOrEmpty;


public class SignURLConfig {
    private HttpMethod method;
    private Date expires;
    private String serviceName;
    private String qualifier;
    private String functionName;
    private String customEndpoint;
    private String escapedPath;
    private Map<String, String> header;
    private Map<String, String[]> queries;

    public SignURLConfig(HttpMethod method, String serviceName, String functionName, Date expires) {
        this.method = method;
        this.serviceName = serviceName;
        this.functionName = functionName;
        this.expires = expires;
    }

    public String signURL(String apiVersion, String endpoint,
                          String akID, String akSec, String stsToken) throws Exception {
        String orignalEscapedPath = this.escapedPath;
        if (isNullOrEmpty(orignalEscapedPath)) {
            orignalEscapedPath = "/";
        }
        String unescapedPath = AcsURLEncoder.decode(new URIBuilder(orignalEscapedPath).getPath());

        String serviceWithQualifier = this.serviceName;
        if (!isNullOrEmpty(this.qualifier)) {
            serviceWithQualifier += "." + this.qualifier;
        }
        String unescapedPathForSign = String.format("/%s/proxy/%s/%s%s",
                apiVersion, serviceWithQualifier, this.functionName, unescapedPath);
        String escapedPath = String.format("/%s/proxy/%s/%s%s",
                apiVersion, serviceWithQualifier, this.functionName, orignalEscapedPath);
        if (!isNullOrEmpty(this.customEndpoint)) {
            endpoint = this.customEndpoint;
            escapedPath = orignalEscapedPath;
            unescapedPathForSign = unescapedPath;
        }

        // make a copy
        HashMap<String, String[]> queries = new HashMap<String, String[]>();
        if (this.queries != null) {
            for (Map.Entry<String, String[]> entry : this.queries.entrySet()) {
                String key = entry.getKey();
                String[] values = entry.getValue();
                if (values != null) {
                    queries.put(key, values.clone());
                }
            }
        }

        queries.put(FcSignatureComposer.authQueryKeyAccessKeyID, new String[]{akID});
        String expiresStr = String.format("%d", this.expires.getTime() / 1000);
        queries.put(FcSignatureComposer.authQueryKeyExpires, new String[]{expiresStr});
        if (!isNullOrEmpty(stsToken)) {
            queries.put(FcSignatureComposer.authQueryKeySecurityToken, new String[]{stsToken});
        }

        String fcResource = FcSignatureComposer.composeStringToSignWithMultiValue(method,
                unescapedPathForSign, this.header, queries);
        String signature = FcSignatureComposer.signString(fcResource, akSec);

        queries.put(FcSignatureComposer.authQueryKeySignature, new String[]{signature});
        return String.format("%s%s?%s", endpoint, escapedPath, FcUtil.concatMultiQueryString(queries));
    }

    public SignURLConfig setQualifier(String qualifier) {
        this.qualifier = qualifier;
        return this;
    }

    public SignURLConfig setCustomEndpoint(String customEndpoint) {
        this.customEndpoint = customEndpoint;
        return this;
    }

    public SignURLConfig setEscapedPath(String escapedPath) {
        this.escapedPath = escapedPath;
        return this;
    }

    public SignURLConfig setHeader(Map<String, String> header) {
        this.header = header;
        return this;
    }

    public SignURLConfig setQueries(Map<String, String[]> queries) {
        this.queries = queries;
        return this;
    }
}
