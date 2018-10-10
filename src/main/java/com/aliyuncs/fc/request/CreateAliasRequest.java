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
import com.aliyuncs.fc.response.CreateAliasResponse;
import com.aliyuncs.fc.utils.ParameterHelper;
import com.google.common.base.Strings;
import com.google.gson.annotations.SerializedName;
import java.util.Map;

public class CreateAliasRequest extends HttpRequest {

    private final String serviceName;

    @SerializedName("aliasName")
    private final String aliasName;
    @SerializedName("versionId")
    private final Integer versionId;
    @SerializedName("description")
    private String description;
    @SerializedName("additionalVersionWeight")
    private Map<Integer, Float> additionalVersionWeight;

    public CreateAliasRequest(String serviceName, String aliasName, Integer versionId) {
        this.serviceName = serviceName;
        this.aliasName = aliasName;
        this.versionId = versionId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getAliasName() {
        return aliasName;
    }

    public Integer getVersionId() {
        return versionId;
    }

    public String getDescription() {
        return description;
    }

    public CreateAliasRequest setDescription(String description) {
        this.description = description;
        return this;
    }

    public Map<Integer, Float> getAdditionalVersionWeight() {
        return additionalVersionWeight;
    }

    public CreateAliasRequest setAdditionalVersionWeight(
        Map<Integer, Float> additionalVersionWeight) {
        this.additionalVersionWeight = additionalVersionWeight;
        return this;
    }

    public byte[] getPayload() {
        return ParameterHelper.ObjectToJson(this).getBytes();
    }

    @Override
    public String getPath() {
        return String.format(Const.ALIAS_PATH, Const.API_VERSION, this.serviceName);
    }

    @Override
    public void validate() throws ClientException {
        if (Strings.isNullOrEmpty(serviceName)) {
            throw new ClientException("Service name cannot be blank");
        }
        if (Strings.isNullOrEmpty(aliasName)) {
            throw new ClientException("Alias name cannot be blank");
        }
    }

    public Class<CreateAliasResponse> getResponseClass() {
        return CreateAliasResponse.class;
    }
}
