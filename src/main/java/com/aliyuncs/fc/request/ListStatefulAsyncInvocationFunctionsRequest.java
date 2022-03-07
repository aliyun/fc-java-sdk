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
import com.aliyuncs.fc.response.ListStatefulAsyncInvocationFunctionsResponse;
import com.google.common.base.Strings;
import java.util.HashMap;
import java.util.Map;

public class ListStatefulAsyncInvocationFunctionsRequest extends HttpRequest {

    private String nextToken;
    private Integer limit;

    public ListStatefulAsyncInvocationFunctionsRequest() {}

    public String getNextToken() {
        return nextToken;
    }

    public ListStatefulAsyncInvocationFunctionsRequest setNextToken(String nextToken) {
        this.nextToken = nextToken;
        return this;
    }

    public Integer getLimit() {
        return limit;
    }

    public ListStatefulAsyncInvocationFunctionsRequest setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public Map<String, String> getQueryParams() {
        Map<String, String> queryParams = new HashMap<String, String>();

        if (limit != null) {
            queryParams.put("limit", limit.toString());
        }

        if (nextToken != null) {
            queryParams.put("nextToken", nextToken);
        }

        return queryParams;
    }

    public void validate() throws ClientException {}

    public String getPath() {
        return String
                .format(Const.LIST_STATEFUL_ASYNC_INVOCATION_FUNCTIONS_PATH, Const.API_VERSION);
    }

    public Class<ListStatefulAsyncInvocationFunctionsResponse> getResponseClass() {
        return ListStatefulAsyncInvocationFunctionsResponse.class;
    }

}