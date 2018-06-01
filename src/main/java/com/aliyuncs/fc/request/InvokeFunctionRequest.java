/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.aliyuncs.fc.request;

import com.aliyuncs.fc.constants.Const;
import com.aliyuncs.fc.constants.HeaderKeys;
import com.aliyuncs.fc.exceptions.ClientException;
import com.aliyuncs.fc.http.HttpRequest;
import com.aliyuncs.fc.response.InvokeFunctionResponse;

import java.util.Map;

import static com.google.common.base.Strings.isNullOrEmpty;
import static java.lang.String.format;

/**
 * TODO: add javadoc
 */
public class InvokeFunctionRequest extends HttpRequest {

    private final String serviceName;
    private final String functionName;
    private String invocationType;
    private String logType;

    private byte[] payload;

    public InvokeFunctionRequest(String serviceName, String functionName) {
        this.serviceName = serviceName;
        this.functionName = functionName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getFunctionName() {
        return functionName;
    }

    public InvokeFunctionRequest setInvocationType(String invocationType) {
        this.invocationType = invocationType;
        return this;
    }

    public String getInvocationType() {
        return invocationType;
    }

    public InvokeFunctionRequest setLogType(String logType) {
        this.logType = logType;
        return this;
    }

    public String getLogType() {
        return logType;
    }

    public InvokeFunctionRequest setPayload(byte[] payload) {
        this.payload = payload;
        return this;
    }

    public Map<String, String> getQueryParams() {
        return null;
    }

    public String getPath() {
        return format(Const.INVOKE_FUNCTION_PATH, Const.API_VERSION, serviceName, functionName);
    }

    public Map<String, String> getHeaders() {
        if (!isNullOrEmpty(invocationType) && !Const.INVOCATION_TYPE_HTTP.equalsIgnoreCase(invocationType)) {
            headers.put(HeaderKeys.INVOCATION_TYPE, invocationType);
        }

        if (!isNullOrEmpty(logType)) {
            headers.put(HeaderKeys.INVOCATION_LOG_TYPE, logType);
        }

        return headers;
    }

    public void validate() throws ClientException {
        if (isNullOrEmpty(serviceName)) {
            throw new ClientException("Service name cannot be blank");
        }
        if (isNullOrEmpty(functionName)) {
            throw new ClientException("Function name cannot be blank");
        }
    }

    public byte[] getPayload() {
        if (this.payload == null || this.payload.length <= 0) {
            return null;
        }
        return payload;
    }

    public Class<InvokeFunctionResponse> getResponseClass() {
        return InvokeFunctionResponse.class;
    }
}
