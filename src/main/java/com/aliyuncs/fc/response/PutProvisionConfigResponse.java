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

import com.aliyuncs.fc.http.HttpResponse;
import com.aliyuncs.fc.model.ProvisionTarget;
import com.aliyuncs.fc.model.ScheduledAction;
import com.google.common.base.Preconditions;

public class PutProvisionConfigResponse extends HttpResponse {

    private ProvisionTarget provisionTarget;

    public PutProvisionConfigResponse(ProvisionTarget provisionTarget) {
        this.provisionTarget = provisionTarget;
    }

    public ProvisionTarget getProvisionTarget() {
        return provisionTarget;
    }

    public PutProvisionConfigResponse setProvisionTarget(ProvisionTarget provisionTarget) {
        this.provisionTarget = provisionTarget;
        return this;
    }

    public String getResource() {
        Preconditions.checkArgument(provisionTarget != null);
        return provisionTarget.getResource();
    }

    public Integer getTarget() {
        Preconditions.checkArgument(provisionTarget != null);
        return provisionTarget.getTarget();
    }

    public ScheduledAction[] getScheduledActions() {
        Preconditions.checkArgument(provisionTarget != null);
        return provisionTarget.getScheduledActions();
    }

}