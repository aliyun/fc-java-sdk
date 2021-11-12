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

import static com.aliyuncs.fc.constants.Const.IF_MATCH_HEADER;

import com.aliyuncs.fc.constants.Const;
import com.aliyuncs.fc.exceptions.ClientException;
import com.aliyuncs.fc.http.HttpRequest;
import com.aliyuncs.fc.model.Code;
import com.aliyuncs.fc.model.CustomContainerConfig;
import com.aliyuncs.fc.model.CustomDNS;
import com.aliyuncs.fc.model.InstanceLifecycleConfig;
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

    @SerializedName("initializer")
    private String initializer;

    @SerializedName("timeout")
    private Integer timeout;

    @SerializedName("initializationTimeout")
    private Integer initializationTimeout;

    @SerializedName("memorySize")
    private Integer memorySize;

    @SerializedName("gpuMemorySize")
    private Integer gpuMemorySize;

    @SerializedName("code")
    private Code code;

    @SerializedName("environmentVariables")
    private Map<String, String> environmentVariables;

    @SerializedName("instanceConcurrency")
    private Integer instanceConcurrency;

    @SerializedName("instanceType")
    private String instanceType;

    @SerializedName("customContainerConfig")
    private CustomContainerConfig customContainerConfig;

    @SerializedName("caPort")
    private Integer caPort;

    @SerializedName("instanceLifecycleConfig")
    private InstanceLifecycleConfig instanceLifecycleConfig;

    @SerializedName("layers")
    private String[] layers;

    @SerializedName("customDNS")
    private CustomDNS customDNS;

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

    public String getDescription() {
        return description;
    }

    public UpdateFunctionRequest setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getRuntime() {
        return runtime;
    }

    public UpdateFunctionRequest setRuntime(String runtime) {
        this.runtime = runtime;
        return this;
    }

    public String getHandler() {
        return handler;
    }

    public UpdateFunctionRequest setHandler(String handler) {
        this.handler = handler;
        return this;
    }

    public UpdateFunctionRequest setInitializer(String initializer) {
        this.initializer = initializer;
        return this;
    }

    public String getInitializer() {
        return initializer;
    }

    public UpdateFunctionRequest setTimeout(Integer timeout) {
        this.timeout = timeout;
        return this;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public UpdateFunctionRequest setInitializationTimeout(Integer initializationTimeout) {
        this.initializationTimeout = initializationTimeout;
        return this;
    }

    public Integer getInitializationTimeout() {
        return initializationTimeout;
    }

    public Integer getMemorySize() {
        return memorySize;
    }

    public UpdateFunctionRequest setMemorySize(Integer memorySize) {
        this.memorySize = memorySize;
        return this;
    }

    public Integer getGpuMemorySize() {
        return gpuMemorySize;
    }

    public UpdateFunctionRequest setGpuMemorySize(Integer gpuMemorySize) {
        this.gpuMemorySize = gpuMemorySize;
        return this;
    }

    public UpdateFunctionRequest setInstanceType(String instanceType) {
        this.instanceType = instanceType;
        return this;
    }

    public String getInstanceType() {
        return instanceType;
    }

    public UpdateFunctionRequest setCustomContainerConfig(CustomContainerConfig customContainerConfig) {
        this.customContainerConfig = customContainerConfig;
        return this;
    }

    public UpdateFunctionRequest setCAPort(Integer caPort){
        this.caPort = caPort;
        return this;
    }

    public Integer getCAPort() {
        return caPort;
    }

    public Code getCode() {
        return code;
    }

    public UpdateFunctionRequest setCode(Code code) {
        this.code = code;
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

    public UpdateFunctionRequest setIfMatch(String ifMatch) {
        this.ifMatch = ifMatch;
        return this;
    }

    public String getPath() {
        return String.format(Const.SINGLE_FUNCTION_PATH, Const.API_VERSION, this.serviceName,
            this.functionName);
    }

    public Map<String, String> getHeaders() {
        if (this.ifMatch != null && this.ifMatch.length() < 0) {
            headers.put(IF_MATCH_HEADER, this.ifMatch);
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

    public Integer getInstanceConcurrency() {
        return instanceConcurrency;
    }

    public void setInstanceConcurrency(Integer instanceConcurrency) {
        this.instanceConcurrency = instanceConcurrency;
    }

    public UpdateFunctionRequest setInstanceLifecycleConfig(InstanceLifecycleConfig instanceLifecycleConfig) {
        this.instanceLifecycleConfig = instanceLifecycleConfig;
        return this;
    }

    public String[] getLayers() {
        return layers;
    }

    public void setLayers(String[] layers) {
        this.layers = layers;
    }

    public CustomDNS getCustomDNS() {
        return customDNS;
    }

    public void setCustomDNS(CustomDNS customDNS) {
        this.customDNS = customDNS;
    }

}
