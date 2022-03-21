package com.aliyuncs.fc.request;

import com.aliyuncs.fc.model.HttpAuthType;
import com.aliyuncs.fc.model.HttpMethod;

import static java.lang.String.format;

/**
 * HTTP Trigger invocation Request for domain 'fcapp.run'
 * @author luoyu
 * @date 2022/3/21
 **/
public class HTTPInvokeFunctionV2Request extends HttpInvokeFunctionRequest{

    public HTTPInvokeFunctionV2Request(String serviceName, String functionName, HttpAuthType authType, HttpMethod method) {
        super(serviceName, functionName, authType, method);
    }

    public HTTPInvokeFunctionV2Request(String serviceName, String functionName, HttpAuthType authType, HttpMethod method, String path) {
        super(serviceName, functionName, authType, method, path);
    }

    @Override
    public String getPath() {
        return format("/%s", this.path);
    }
}
