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
import com.aliyuncs.fc.model.SortOrder;
import com.aliyuncs.fc.constants.Const;
import com.aliyuncs.fc.response.ListStatefulAsyncInvocationsResponse;

import com.google.common.base.Strings;
import java.util.HashMap;
import java.util.Map;


public class ListStatefulAsyncInvocationsRequest extends HttpRequest {

    private String serviceName;
    private String functionName;
    private String qualifier;
    private String nextToken;
    private Integer limit;
    private String startedTimeBegin;
    private String startedTimeEnd;
    private String status;
    private SortOrder sortOrderByTime;
    private String invocationIdPrefix;
    private boolean includePayload;

    public ListStatefulAsyncInvocationsRequest(String serviceName, String functionName) {
        this.serviceName = serviceName;
        this.functionName = functionName;
        this.includePayload = false;
    }

    public String getServiceName() { return serviceName; }

    public String getFunctionName() {
        return functionName;
    }

    public String getQualifier() {
        return qualifier;
    }

    public String getNextToken() {
        return nextToken;
    }

    public Integer getLimit() {
        return limit;
    }

    public String getStartedTimeBegin() {
        return startedTimeBegin;
    }

    public String getStartedTimeEnd() {
        return startedTimeEnd;
    }

    public String getStatus() {
        return status;
    }

    public SortOrder getSortOrderByTime() {
        return sortOrderByTime;
    }

    public String getInvocationIdPrefix() {
        return invocationIdPrefix;
    }

    public boolean getIncludePayload() {
        return includePayload;
    }


    public ListStatefulAsyncInvocationsRequest setQualifier(String qualifier) {
        this.qualifier = qualifier;
        return this;
    }

    public ListStatefulAsyncInvocationsRequest setNextToken(String nextToken) {
        this.nextToken = nextToken;
        return this;
    }

    public ListStatefulAsyncInvocationsRequest setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public ListStatefulAsyncInvocationsRequest setStartedTimeBegin(String startedTimeBegin) {
        this.startedTimeBegin = startedTimeBegin;
        return this;
    }

    public ListStatefulAsyncInvocationsRequest setStartedTimeEnd(String startedTimeEnd) {
        this.startedTimeEnd = startedTimeEnd;
        return this;
    }

    public ListStatefulAsyncInvocationsRequest setStatus(String status) {
        this.status = status;
        return this;
    }

    public ListStatefulAsyncInvocationsRequest setInvocationIdPrefix(String invocationIdPrefix) {
        this.invocationIdPrefix = invocationIdPrefix;
        return this;
    }

    public ListStatefulAsyncInvocationsRequest setIncludePayload(boolean includePayload) {
        this.includePayload = includePayload;
        return this;
    }

    public ListStatefulAsyncInvocationsRequest setSortOrderByTime(SortOrder sortOrderByTime) {
        this.sortOrderByTime = sortOrderByTime;
        return this;
    }

    public Map<String, String> getQueryParams() {
        Map<String, String> queryParams = new HashMap<String, String>();

        if (limit != null) {
            queryParams.put("limit", limit.toString());
        }

        if (!Strings.isNullOrEmpty(nextToken)) {
            queryParams.put("nextToken", nextToken);
        }

        if (!Strings.isNullOrEmpty(startedTimeBegin)) {
            queryParams.put("startedTimeBegin", startedTimeBegin);
        }

        if (!Strings.isNullOrEmpty(startedTimeEnd)) {
            queryParams.put("startedTimeEnd", startedTimeEnd);
        }

        if (!Strings.isNullOrEmpty(status)) {
            queryParams.put("status", status);
        }

        if (sortOrderByTime != null) {
            queryParams.put("sortOrderByTime", sortOrderByTime.toString());
        }

        if (!Strings.isNullOrEmpty(invocationIdPrefix)) {
            queryParams.put("invocationIdPrefix", invocationIdPrefix);
        }

        queryParams.put("includePayload",  String.valueOf(includePayload));

        return queryParams;
    }

    public void validate() throws ClientException {
        if (Strings.isNullOrEmpty(serviceName)) {
            throw new ClientException("Service name cannot be blank");
        }
        if (Strings.isNullOrEmpty(functionName)) {
            throw new ClientException("Function name cannot be blank");
        }
    }

    public String getPath() {
        if (Strings.isNullOrEmpty(qualifier)) {
            return String.format(Const.LIST_FUNCTION_STATEFUL_ASYNC_INVOCATIONS_PATH, Const.API_VERSION, serviceName, functionName);
        }
        return String.format(Const.LIST_FUNCTION_WITH_QUALIFIER_STATEFUL_ASYNC_INVOCATIONS_PATH, Const.API_VERSION, this.serviceName, this.qualifier, this.functionName);
    }

    public Class<ListStatefulAsyncInvocationsResponse> getResponseClass() {
        return ListStatefulAsyncInvocationsResponse.class;
    }

}
