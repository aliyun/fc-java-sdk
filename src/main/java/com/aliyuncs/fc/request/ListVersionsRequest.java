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
import com.aliyuncs.fc.response.ListVersionsResponse;
import com.google.common.base.Strings;
import java.util.Map;

public class ListVersionsRequest extends HttpRequest {
    public final static String LIST_DIRECTION_BACKWARD = "BACKWARD";
    public final static String LIST_DIRECTION_FORWARD = "FORWARD";

    private final String serviceName;
    private String startKey;
    private String nextToken;
    private Integer limit;
    private String direction;

    public ListVersionsRequest(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getStartKey() {
        return startKey;
    }

    public ListVersionsRequest setStartKey(String startKey) {
        this.startKey = startKey;
        return this;
    }

    public String getNextToken() {
        return nextToken;
    }

    public ListVersionsRequest setNextToken(String nextToken) {
        this.nextToken = nextToken;
        return this;
    }

    public Integer getLimit() {
        return limit;
    }

    public ListVersionsRequest setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public String getDirection() {
        return direction;
    }

    public ListVersionsRequest setDirection(String direction) {
        this.direction = direction;
        return this;
    }

    public Map<String, String> getQueryParams() {
        return ListRequestUrlHelper.buildListVersionParams(startKey, nextToken, limit, direction);
    }

    public byte[] getPayload() {
        return null;
    }

    public void validate() throws ClientException {
        if (Strings.isNullOrEmpty(this.serviceName)) {
            throw new ClientException("Service name cannot be blank");
        }
    }

    public String getPath() {
        return String
            .format(Const.SERVICE_VERSION_PATH, Const.API_VERSION, this.serviceName);
    }

    public Class<ListVersionsResponse> getResponseClass() {
        return ListVersionsResponse.class;
    }
}
