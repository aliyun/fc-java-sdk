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

import static com.aliyuncs.fc.constants.Const.IF_MATCH_HEADER;

import com.aliyuncs.fc.constants.Const;
import com.aliyuncs.fc.exceptions.ClientException;
import com.aliyuncs.fc.http.HttpRequest;
import com.aliyuncs.fc.response.PublishVersionResponse;
import com.aliyuncs.fc.utils.ParameterHelper;
import com.google.common.base.Strings;
import com.google.gson.annotations.SerializedName;
import java.util.Map;

public class PublishVersionRequest extends HttpRequest {

    private transient final String serviceName;
    private transient String ifMatch;

    @SerializedName("description")
    private String description;

    public PublishVersionRequest(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getDescription() {
        return description;
    }

    public PublishVersionRequest setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getIfMatch() {
        return ifMatch;
    }

    public PublishVersionRequest setIfMatch(String ifMatch) {
        this.ifMatch = ifMatch;
        return this;
    }

    @Override
    public String getPath() {
        return String.format(Const.SERVICE_VERSION_PATH, Const.API_VERSION, this.serviceName);
    }

    public Map<String, String> getHeaders() {
        if (!Strings.isNullOrEmpty(ifMatch)) {
            headers.put(IF_MATCH_HEADER, ifMatch);
        }
        return headers;
    }

    @Override
    public Map<String, String> getQueryParams() {
        return null;
    }

    @Override
    public byte[] getPayload() {
        return ParameterHelper.ObjectToJson(this).getBytes();
    }

    @Override
    public void validate() throws ClientException {
        if (Strings.isNullOrEmpty(serviceName)) {
            throw new ClientException("Service name cannot be blank");
        }
    }

    public Class<PublishVersionResponse> getResponseClass() {
        return PublishVersionResponse.class;
    }

}
