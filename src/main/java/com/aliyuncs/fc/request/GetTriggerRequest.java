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
import com.aliyuncs.fc.response.GetTriggerResponse;

import com.google.common.base.Strings;
import java.util.Map;

/**
 * TODO: add javadoc
 */
public class GetTriggerRequest extends HttpRequest {

    private final String serviceName;
    private final String functionName;
    private final String triggerName;

    public GetTriggerRequest(String serviceName, String functionName, String triggerName) {
        this.serviceName = serviceName;
        this.functionName = functionName;
        this.triggerName = triggerName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getTriggerName() {
        return triggerName;
    }

    public Map<String, String> getQueryParams() {
        return null;
    }

    public String getPath() {
        return String
            .format(Const.SINGLE_TRIGGER_PATH, Const.API_VERSION, this.serviceName, this.functionName,
                this.triggerName);
    }

    public byte[] getPayload() {
        return null;
    }

    public void validate() throws ClientException {
        if (Strings.isNullOrEmpty(serviceName)) {
            throw new ClientException("Service name cannot be blank");
        }
        if (Strings.isNullOrEmpty(functionName)) {
            throw new ClientException("Function name cannot be blank");
        }
        if (Strings.isNullOrEmpty(triggerName)) {
            throw new ClientException("Trigger name cannot be blank");
        }
    }

    public Class<GetTriggerResponse> getResponseClass() {
        return GetTriggerResponse.class;
    }

}
