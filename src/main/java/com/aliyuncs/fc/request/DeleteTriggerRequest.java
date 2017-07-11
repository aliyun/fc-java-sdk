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
import com.aliyuncs.fc.response.DeleteTriggerResponse;

import com.google.common.base.Strings;
import java.util.Map;

/**
 * TODO: add javadoc
 */
public class DeleteTriggerRequest extends HttpRequest {

    private final String serviceName;
    private final String functionName;
    private final String triggerName;
    private transient String ifMatch;

    public DeleteTriggerRequest(String serviceName, String functionName, String triggerName) {
        this.serviceName = serviceName;
        this.functionName = functionName;
        this.triggerName = triggerName;
    }

    public DeleteTriggerRequest setIfMatch(String ifMath) {
        this.ifMatch = ifMath;
        return this;
    }

    public String getIfMatch() {
        return ifMatch;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getFunctionName() {
        return functionName;
    }

    public String getTriggerName() {
        return triggerName;
    }

    public String getPath() {
        return String
            .format(Const.SINGLE_TRIGGER_PATH, Const.API_VERSION, this.serviceName, this.functionName,
                this.triggerName);
    }

    public Map<String, String> getQueryParams() {
        return null;
    }

    public Map<String, String> getHeaders() {
        if (!Strings.isNullOrEmpty(ifMatch)) {
            headers.put("If-Match", this.ifMatch);
        }
        return headers;
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

    public Class<DeleteTriggerResponse> getResponseClass() {
        return DeleteTriggerResponse.class;
    }

}
