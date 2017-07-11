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

import com.aliyuncs.fc.exceptions.ClientException;
import com.aliyuncs.fc.http.HttpRequest;
import com.aliyuncs.fc.constants.Const;
import com.aliyuncs.fc.response.CreateTriggerResponse;
import com.aliyuncs.fc.utils.ParameterHelper;

import com.google.common.base.Strings;
import com.google.gson.annotations.SerializedName;
import java.util.Map;

/**
 * TODO: add javadoc
 */
public class CreateTriggerRequest extends HttpRequest {

    private transient final String serviceName;
    private transient final String functionName;

    @SerializedName("triggerName")
    private String triggerName;

    @SerializedName("sourceArn")
    private String sourceArn;

    @SerializedName("triggerType")
    private String triggerType;

    @SerializedName("invocationRole")
    private String invocationRole;

    @SerializedName("triggerConfig")
    private Object triggerConfig;

    public CreateTriggerRequest(String serviceName, String functionName) {
        this.serviceName = serviceName;
        this.functionName = functionName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getFunctionName() {
        return functionName;
    }

    public CreateTriggerRequest setTriggerName(String triggerName) {
        this.triggerName = triggerName;
        return this;
    }

    public String getTriggerName() {
        return triggerName;
    }

    public CreateTriggerRequest setSourceArn(String sourceArn) {
        this.sourceArn = sourceArn;
        return this;
    }

    public String getSourceArn() {
        return sourceArn;
    }

    public CreateTriggerRequest setTriggerType(String triggerType) {
        this.triggerType = triggerType;
        return this;
    }

    public String getTriggerType() {
        return triggerType;
    }

    public CreateTriggerRequest setInvocationRole(String invocationRole) {
        this.invocationRole = invocationRole;
        return this;
    }

    public String getInvocationRole() {
        return invocationRole;
    }

    public CreateTriggerRequest setTriggerConfig(Object triggerConfig) {
        this.triggerConfig = triggerConfig;
        return this;
    }

    public Object getTriggerConfig() {
        return triggerConfig;
    }

    public String getPath() {
        return String
            .format(Const.TRIGGER_PATH, Const.API_VERSION, this.serviceName, this.functionName);
    }

    public Map<String, String> getQueryParams() {
        return null;
    }

    public byte[] getPayload() {
        return ParameterHelper.ObjectToJson(this).getBytes();
    }

    public void validate() throws ClientException {
        if (Strings.isNullOrEmpty(serviceName)) {
            throw new ClientException("Service name cannot be blank");
        }
        if (Strings.isNullOrEmpty(functionName)) {
            throw new ClientException("Function name cannot be blank");
        }
        return;
    }

    public Class<CreateTriggerResponse> getResponseClass() {
        return CreateTriggerResponse.class;
    }
}
