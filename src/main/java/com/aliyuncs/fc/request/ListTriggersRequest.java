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
import com.aliyuncs.fc.model.ListRequestUrlHelper;
import com.aliyuncs.fc.response.ListTriggersResponse;
import com.google.common.base.Strings;
import java.util.Map;

/**
 * TODO: add javadoc
 */
public class ListTriggersRequest extends HttpRequest {
    private final String serviceName;
    private final String functionName;
    private String prefix;
    private String startKey;
    private String nextToken;
    private Integer limit;

    public ListTriggersRequest(String serviceName, String functionName) {
        this.serviceName = serviceName;
        this.functionName = functionName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getFunctionName() {
        return functionName;
    }

    public ListTriggersRequest setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public String getPrefix() {
        return prefix;
    }

    public ListTriggersRequest setStartKey(String startKey) {
        this.startKey = startKey;
        return this;
    }

    public String getStartKey() {
        return startKey;
    }

    public ListTriggersRequest setNextToken(String nextToken) {
        this.nextToken = nextToken;
        return this;
    }

    public String getNextToken() {
        return nextToken;
    }

    public ListTriggersRequest setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public Integer getLimit() {
        return limit;
    }

    public Map<String, String> getQueryParams() {
        return ListRequestUrlHelper.buildParams(prefix, startKey, nextToken, limit);
    }

    public byte[] getPayload() {
        return null;
    }

    public void validate() throws ClientException {
        if (Strings.isNullOrEmpty(this.serviceName)) {
            throw new ClientException("Service name cannot be blank");
        }
        if (Strings.isNullOrEmpty(this.functionName)) {
            throw new ClientException("Function name cannot be blank");
        }
    }

    public String getPath() {
        return String
            .format(Const.TRIGGER_PATH, Const.API_VERSION, this.serviceName, this.functionName);
    }

    public Class<ListTriggersResponse> getResponseClass() {
        return ListTriggersResponse.class;
    }

}
