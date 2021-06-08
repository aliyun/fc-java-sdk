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
import com.aliyuncs.fc.response.GetStatefulAsyncInvocationResponse;
import com.google.common.base.Strings;

public class GetStatefulAsyncInvocationRequest extends HttpRequest {

    private final transient String serviceName;
    private final transient String qualifier;
    private final transient String functionName;
    private final transient String invocationId;

    public GetStatefulAsyncInvocationRequest(String serviceName, String qualifier, String functionName, String invocationId) {
        this.serviceName = serviceName;
        this.qualifier = qualifier;
        this.functionName = functionName;
        this.invocationId = invocationId;
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

    public String getInvocationId() {
        return invocationId;
    }

    @Override
    public String getPath() {
        if (Strings.isNullOrEmpty(qualifier)) {
            return String.format(Const.GET_FUNCTION_STATEFUL_ASYNC_INVOCATION_PATH, Const.API_VERSION, this.serviceName, this.functionName, this.invocationId);
        }
        return String.format(Const.GET_FUNCTION_WITH_QUALIFIER_STATEFUL_ASYNC_INVOCATION_PATH, Const.API_VERSION, this.serviceName, this.qualifier, this.functionName, this.invocationId);
    }

    @Override
    public void validate() throws ClientException {
        if (Strings.isNullOrEmpty(serviceName)) {
            throw new ClientException("Service name cannot be blank");
        }
        if (Strings.isNullOrEmpty(functionName)) {
            throw new ClientException("Function name cannot be blank");
        }
        if (Strings.isNullOrEmpty(invocationId)) {
            throw new ClientException("InvocationId cannot be blank");
        }
    }

    public Class<GetStatefulAsyncInvocationResponse> getResponseClass() {
        return GetStatefulAsyncInvocationResponse.class;
    }
}
