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
import com.aliyuncs.fc.model.AsyncConfig;
import com.aliyuncs.fc.response.PutFunctionAsyncConfigResponse;
import com.aliyuncs.fc.utils.ParameterHelper;
import com.google.common.base.Strings;
import java.util.Map;

public class PutFunctionAsyncConfigRequest extends HttpRequest {
    private final transient String serviceName;
    private final transient String qualifier;
    private final transient String functionName;

    private AsyncConfig asyncConfig;

    public PutFunctionAsyncConfigRequest(String serviceName, String qualifier, String functionName) {
        this.serviceName = serviceName;
        this.qualifier = qualifier;
        this.functionName = functionName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getQualifier() {
        return qualifier;
    }

    public String getFunctionName() {
        return functionName;
    }

    public PutFunctionAsyncConfigRequest setAsyncConfig(AsyncConfig config) {
        this.asyncConfig = config;
        return this;
    }

    public AsyncConfig getAsyncConfig() {
        return asyncConfig;
    }

    public byte[] getPayload() {
        return ParameterHelper.ObjectToJson(this.asyncConfig).getBytes();
    }

    @Override
    public String getPath() {
        if (qualifier.isEmpty()) {
            return String.format(Const.SINGLE_FUNCTION_ASYNC_CONFIG_PATH, Const.API_VERSION, this.serviceName, this.functionName);
        }
        return String.format(Const.SINGLE_FUNCTION_With_QUALIFIER_ASYNC_CONFIG_PATH, Const.API_VERSION, this.serviceName, this.qualifier, this.functionName);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public void validate() throws ClientException {
        if (Strings.isNullOrEmpty(serviceName)) {
            throw new ClientException("Service name cannot be blank");
        }
        if (Strings.isNullOrEmpty(functionName)) {
            throw new ClientException("Function name cannot be blank");
        }
    }

    public Class<PutFunctionAsyncConfigResponse> getResponseClass() {
        return PutFunctionAsyncConfigResponse.class;
    }
}