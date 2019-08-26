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
import com.aliyuncs.fc.response.GetServiceResponse;
import com.google.common.base.Strings;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class GetResourceTagsRequest  extends HttpRequest {
    @SerializedName("resourceArn")
    private String resourceArn;

    public GetResourceTagsRequest(){}

    public GetResourceTagsRequest(String resourceArn) {
        this.resourceArn = resourceArn;
    }

    public String getResourceArn() {
        return resourceArn;
    }

    public void setResourceArn(String resourceArn) {
        this.resourceArn = resourceArn;
    }

    public String getPath() {
        return String.format(Const.TAG_PATH, Const.API_VERSION);
    }

    public Map<String, String> getQueryParams() {
        Map<String, String> queryParams = new HashMap<String, String>();
        if (resourceArn != null) {
            queryParams.put("resourceArn", resourceArn);
        }
        return queryParams;
    }

    public void validate() throws ClientException {
        if (Strings.isNullOrEmpty(resourceArn)) {
            throw new ClientException("resourceArn cannot be blank");
        }
    }

    public Class<GetServiceResponse> getResponseClass() {
        return GetServiceResponse.class;
    }
}
