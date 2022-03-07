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

package com.aliyuncs.fc.response;

import com.aliyuncs.fc.model.AsyncConfig;
import com.aliyuncs.fc.http.HttpResponse;
import com.aliyuncs.fc.model.FunctionCoreMetaRef;
import com.aliyuncs.fc.model.StatefulAsyncInvocation;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class ListStatefulAsyncInvocationFunctionsResponse extends HttpResponse {

    @SerializedName("data")
    private FunctionCoreMetaRef[] data;
    @SerializedName("nextToken")
    private String nextToken;

    public FunctionCoreMetaRef[] getFunctionCoreMetaRef() {
        return data;
    }

    public ListStatefulAsyncInvocationFunctionsResponse setFunctionCoreMetaRef(FunctionCoreMetaRef[] data) {
        this.data = data;
        return this;
    }

    public String getNextToken() {
        return nextToken;
    }

    public ListStatefulAsyncInvocationFunctionsResponse setNextToken(String nextToken) {
        this.nextToken = nextToken;
        return this;
    }
}