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
import com.aliyuncs.fc.response.PutOnDemandConfigResponse;
import com.aliyuncs.fc.model.OnDemandConfigMetadata;
import com.aliyuncs.fc.constants.Const;
import com.aliyuncs.fc.utils.ParameterHelper;

/**
 * TODO: add javadoc
 */
public class PutOnDemandConfigRequest extends BaseOnDemandConfigRequest {

    OnDemandConfigMetadata onDemandConfigMetadata;

    public PutOnDemandConfigRequest(String serviceName, String qualifier, String functionName, int maximumInstanceCount) {
        this.serviceName = serviceName;
        this.qualifier = qualifier;
        this.functionName = functionName;

        String resource = String.format(Const.SINGLE_FUNCTION_WITH_QUALIFIER_PATH, Const.API_VERSION, serviceName, qualifier, functionName);
        this.onDemandConfigMetadata = new OnDemandConfigMetadata(resource, maximumInstanceCount);
    }

    @Override
    public byte[] getPayload() {
        return ParameterHelper.ObjectToJson(this.onDemandConfigMetadata).getBytes();
    }

    public Class<PutOnDemandConfigResponse> getResponseClass() {
        return PutOnDemandConfigResponse.class;
    }
}


