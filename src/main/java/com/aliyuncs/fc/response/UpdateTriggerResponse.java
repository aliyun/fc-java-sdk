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
import com.aliyuncs.fc.model.TriggerMetadata;

import com.google.common.base.Preconditions;
import java.util.Map;

/**
 * TODO: add javadoc
 */
public class UpdateTriggerResponse extends HttpResponse {

    private TriggerMetadata triggerMetadata;

    public UpdateTriggerResponse setTriggerMetadata(TriggerMetadata triggerMetadata) {
        this.triggerMetadata = triggerMetadata;
        return this;
    }

    public String getTriggerName() {
        Preconditions.checkArgument(triggerMetadata != null);
        return triggerMetadata.getTriggerName();
    }

    public String getSourceArn() {
        Preconditions.checkArgument(triggerMetadata != null);
        return triggerMetadata.getSourceArn();
    }

    public String getTriggerType() {
        Preconditions.checkArgument(triggerMetadata != null);
        return triggerMetadata.getTriggerType();
    }

    public String getInvocationRole() {
        Preconditions.checkArgument(triggerMetadata != null);
        return triggerMetadata.getInvocationRole();
    }

    public String getCreatedTime() {
        Preconditions.checkArgument(triggerMetadata != null);
        return triggerMetadata.getCreatedTime();
    }

    public String getLastModifiedTime() {
        Preconditions.checkArgument(triggerMetadata != null);
        return triggerMetadata.getLastModifiedTime();
    }

    public Object getTriggerConfig() {
        Preconditions.checkArgument(triggerMetadata != null);
        return triggerMetadata.getTriggerConfig();
    }
}
