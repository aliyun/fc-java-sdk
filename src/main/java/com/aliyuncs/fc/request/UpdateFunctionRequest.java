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
import com.aliyuncs.fc.exceptions.ClientException;
import com.aliyuncs.fc.http.HttpRequest;
import com.aliyuncs.fc.model.Code;
import com.aliyuncs.fc.response.UpdateFunctionResponse;
import com.aliyuncs.fc.utils.ParameterHelper;

import com.google.common.base.Strings;
import com.google.gson.annotations.SerializedName;
import java.util.Map;

/**
 * TODO: add javadoc
 */
public class UpdateFunctionRequest extends HttpRequest {

    private transient final String serviceName;
    private transient final String functionName;

    @SerializedName("description")
    private String description;

    @SerializedName("runtime")
    private String runtime;

    @SerializedName("handler")
    private String handler;

    @SerializedName("timeout")
    private Integer timeout;

    @SerializedName("memorySize")
    private Integer memorySize;

    @SerializedName("code")
    private Code code;

    @SerializedName("environmentVariables")
    private Map<String, String> environmentVariables;

    private transient String ifMatch;


    public UpdateFunctionRequest(String serviceName, String functionName) {
        this.serviceName = serviceName;
        this.functionName = functionName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getFunctionName() {
        return functionName;
    }

    public UpdateFunctionRequest setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public UpdateFunctionRequest setRuntime(String runtime) {
        this.runtime = runtime;
        return this;
    }

    public String getRuntime() {
        return runtime;
    }

    public UpdateFunctionRequest setHandler(String handler) {
        this.handler = handler;
        return this;
    }

    public String getHandler() {
        return handler;
    }

    public UpdateFunctionRequest setTimeout(Integer timeout) {
        this.timeout = timeout;
        return this;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public UpdateFunctionRequest setMemorySize(Integer memorySize) {
        this.memorySize = memorySize;
        return this;
    }

    public Integer getMemorySize() {
        return memorySize;
    }

    public UpdateFunctionRequest setCode(Code code) {
        this.code = code;
        return this;
    }

    public Code getCode() {
        return code;
    }

    public UpdateFunctionRequest setIfMatch(String ifMatch) {
        this.ifMatch = ifMatch;
        return this;
    }

    public Map<String, String> getEnvironmentVariables() {
        return environmentVariables;
    }

    public void setEnvironmentVariables(Map<String, String> environmentVariables) {
        this.environmentVariables = environmentVariables;
    }

    public String getIfMatch() {
        return ifMatch;
    }

    public String getPath() {
        return String.format(Const.SINGLE_FUNCTION_PATH, Const.API_VERSION, this.serviceName,
            this.functionName);
    }

    public Map<String, String> getHeaders() {
        if (this.ifMatch != null && this.ifMatch.length() < 0) {
            headers.put("If-Match", this.ifMatch);
        }
        return headers;
    }

    public byte[] getPayload() {
        return ParameterHelper.ObjectToJson(this).getBytes();
    }

    public Map<String, String> getQueryParams() {
        return null;
    }

    public void validate() throws ClientException {
        if (Strings.isNullOrEmpty(serviceName)) {
            throw new ClientException("Service name cannot be blank");
        }
        if (Strings.isNullOrEmpty(functionName)) {
            throw new ClientException("Function name cannot be blank");
        }
    }

    public Class<UpdateFunctionResponse> getResponseClass() {
        return UpdateFunctionResponse.class;
    }
}
