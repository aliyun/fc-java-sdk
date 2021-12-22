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
import com.aliyuncs.fc.response.ListFunctionsResponse;
import com.aliyuncs.fc.response.ListInstancesResponse;
import com.google.common.base.Strings;
import com.google.gson.annotations.SerializedName;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO: add javadoc
 */
public class ListInstancesRequest extends HttpRequest {

    private final String serviceName;
    private final String functionName;
    private Integer limit;
    private String qualifier;
    private String[] instanceIds;
    protected boolean queryUsingArray = true;

    public ListInstancesRequest(String serviceName, String functionName) {
        this.serviceName = serviceName;
        this.functionName = functionName;
    }

    public Integer getLimit() {
        return limit;
    }

    public ListInstancesRequest setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public String getQualifier() {
        return qualifier;
    }

    public ListInstancesRequest setQualifier(String qualifier) {
        this.qualifier = qualifier;
        return this;
    }

    public String getFunctionName() {
        return functionName;
    }

    public String[] getInstanceIds() {
        return instanceIds;
    }

    public ListInstancesRequest setInstanceIds(String[] instanceIds) {
        this.instanceIds = instanceIds;
        return this;
    }

    public String getPath() {
        if (Strings.isNullOrEmpty(qualifier)) {
            return String.format(Const.LIST_INSTANCES_PATH, Const.API_VERSION, this.serviceName, this.functionName);
        } else {
            return String.format(Const.LIST_INSTANCES_WITH_QUALIFIER_PATH, Const.API_VERSION, this.serviceName,
                    this.qualifier, this.functionName);
        }
    }

    public Map<String, String> getQueryParams() {
        Map<String, String> queryParams = new HashMap<String, String>();
        Gson gson = new Gson();
        if (this.limit != null && this.limit > 0) {
            queryParams.put("limit", String.valueOf(this.limit));
        }
        if (this.instanceIds != null && this.instanceIds.length > 0) {
            queryParams.put("instanceIds", gson.toJson(this.instanceIds));
        }
        return queryParams;
    }

    public byte[] getPayload() {
        return null;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void validate() throws ClientException {
        if (Strings.isNullOrEmpty(serviceName)) {
            throw new ClientException("Service name cannot be blank");
        }
    }

    public Class<ListInstancesResponse> getResponseClass() {
        return ListInstancesResponse.class;
    }
}
