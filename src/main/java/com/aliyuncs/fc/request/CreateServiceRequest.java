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
import com.aliyuncs.fc.http.FormatType;
import com.aliyuncs.fc.http.HttpRequest;
import com.aliyuncs.fc.model.LogConfig;
import com.aliyuncs.fc.model.NasConfig;
import com.aliyuncs.fc.model.VpcConfig;
import com.aliyuncs.fc.response.CreateServiceResponse;
import com.aliyuncs.fc.utils.ParameterHelper;
import com.google.gson.annotations.SerializedName;
import java.util.Map;

/**
 * TODO: add javadoc
 */
public class CreateServiceRequest extends HttpRequest {

    @SerializedName("serviceName")
    private String serviceName;

    @SerializedName("description")
    private String description;

    @SerializedName("role")
    private String role;

    @SerializedName("logConfig")
    private LogConfig logConfig;

    @SerializedName("vpcConfig")
    private VpcConfig vpcConfig;

    @SerializedName("internetAccess")
    private Boolean internetAccess;

    @SerializedName("nasConfig")
    private NasConfig nasConfig;

    public String getServiceName() {
        return serviceName;
    }

    public CreateServiceRequest setServiceName(String serviceName) {
        this.serviceName = serviceName;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public CreateServiceRequest setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getRole() {
        return role;
    }

    public CreateServiceRequest setRole(String role) {
        this.role = role;
        return this;
    }

    public LogConfig getLogConfig() {
        return logConfig;
    }

    public CreateServiceRequest setLogConfig(LogConfig logConfig) {
        this.logConfig = logConfig;
        return this;
    }

    public String getPath() {
        return String.format(Const.SERVICE_PATH, Const.API_VERSION);
    }

    public byte[] getPayload() {
        if (ParameterHelper.ObjectToJson(this) != null) {
            return ParameterHelper.ObjectToJson(this).getBytes();
        }
        return null;
    }

    public FormatType getForm() {
        return FormatType.JSON;
    }

    public void validate() throws ClientException {
        return;
    }

    public Map<String, String> getQueryParams() {
        return null;
    }

    public Class<CreateServiceResponse> getResponseClass() {
        return CreateServiceResponse.class;
    }

    public VpcConfig getVpcConfig() {
        return vpcConfig;
    }

    public void setVpcConfig(VpcConfig vpcConfig) {
        this.vpcConfig = vpcConfig;
    }

    public Boolean getInternetAccess() {
        return internetAccess;
    }

    public void setInternetAccess(Boolean internetAccess) {
        this.internetAccess = internetAccess;
    }

    public NasConfig getNasConfig() {
        return nasConfig;
    }

    public void setNasConfig(NasConfig nasConfig) {
        this.nasConfig = nasConfig;
    }
}
