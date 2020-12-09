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
import com.aliyuncs.fc.model.ScheduledAction;
import com.aliyuncs.fc.model.TargetTrackingPolicy;
import com.aliyuncs.fc.response.PutProvisionConfigResponse;
import com.aliyuncs.fc.utils.ParameterHelper;
import com.google.common.base.Strings;
import com.google.gson.annotations.SerializedName;
import java.util.Map;

public class PutProvisionConfigRequest extends HttpRequest {
    private final transient String serviceName;
    private final transient String qualifier;
    private final transient String functionName;
    private transient String ifMatch;

    @SerializedName("target")
    private Integer target;

    @SerializedName("scheduledActions")
    private ScheduledAction[] scheduledActions;

    @SerializedName("targetTrackingPolicies")
    private TargetTrackingPolicy[] targetTrackingPolicies;

    public PutProvisionConfigRequest(String serviceName, String qualifier, String functionName) {
        this.serviceName = serviceName;
        this.qualifier = qualifier;
        this.functionName = functionName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getQualifier() {
        return qualifier;
    }

    public String getFunctionName() {
        return functionName;
    }

    public PutProvisionConfigRequest setTarget(Integer target) {
        this.target = target;
        return this;
    }

    public Integer getTarget() {
        return target;
    }

    public ScheduledAction[] getScheduledActions() {
        return scheduledActions;
    }

    public void setScheduledActions(ScheduledAction[] scheduledActions) {
        this.scheduledActions = scheduledActions;
    }

    public TargetTrackingPolicy[] getTargetTrackingPolicies() {
        return targetTrackingPolicies;
    }

    public void setTargetTrackingPolicies(TargetTrackingPolicy[] targetTrackingPolicies) {
        this.targetTrackingPolicies = targetTrackingPolicies;
    }

    public String getIfMatch() {
        return ifMatch;
    }

    public PutProvisionConfigRequest setIfMatch(String ifMatch) {
        this.ifMatch = ifMatch;
        return this;
    }

    public byte[] getPayload() {
        return ParameterHelper.ObjectToJson(this).getBytes();
    }

    @Override
    public String getPath() {
        return String.format(Const.SINGLE_PROVISION_CONFIG_PATH, Const.API_VERSION, this.serviceName, this.qualifier, this.functionName);
    }

    public Map<String, String> getHeaders() {
        if (this.ifMatch != null && this.ifMatch.length() < 0) {
            headers.put(IF_MATCH_HEADER, this.ifMatch);
        }
        return headers;
    }

    @Override
    public void validate() throws ClientException {
        if (Strings.isNullOrEmpty(serviceName)) {
            throw new ClientException("Service name cannot be blank");
        }
        if (Strings.isNullOrEmpty(qualifier)) {
            throw new ClientException("Qualifier cannot be blank");
        }
        if (Strings.isNullOrEmpty(functionName)) {
            throw new ClientException("Function name cannot be blank");
        }
    }

    public Class<PutProvisionConfigResponse> getResponseClass() {
        return PutProvisionConfigResponse.class;
    }
}