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
import com.aliyuncs.fc.model.RouteConfig;
import com.aliyuncs.fc.response.CreateCustomDomainResponse;
import com.aliyuncs.fc.utils.ParameterHelper;
import com.google.gson.annotations.SerializedName;
import java.util.Map;

public class CreateCustomDomainRequest extends HttpRequest {

    @SerializedName("domainName")
    private String domainName;

    @SerializedName("protocol")
    private String protocol;

    @SerializedName("routeConfig")
    private RouteConfig routes;

    public CreateCustomDomainRequest(String domainName, String protocol, RouteConfig routes) {
        this.domainName = domainName;
        this.protocol = protocol;
        this.routes = routes;
    }

    public CreateCustomDomainRequest() {
    }

    public String getDomainName() {
        return domainName;
    }

    public CreateCustomDomainRequest setDomainName(String domainName) {
        this.domainName = domainName;
        return this;
    }

    public String getProtocol() {
        return protocol;
    }

    public CreateCustomDomainRequest setProtocol(String protocol) {
        this.protocol = protocol;
        return this;
    }

    public RouteConfig getRouteConfig() {
        return routes;
    }

    public CreateCustomDomainRequest setRouteConfig(RouteConfig routes) {
        this.routes = routes;
        return this;
    }

    public String getPath() {
        return String.format(Const.CUSTOM_DOMAIN_PATH, Const.API_VERSION);
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

    public Class<CreateCustomDomainResponse> getResponseClass() {
        return CreateCustomDomainResponse.class;
    }
}
