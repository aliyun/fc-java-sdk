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

package com.aliyuncs.fc.model;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class ProvisionTarget {
    @SerializedName("resource")
    private String resource;

    @SerializedName("target")
    private Integer target;

    @SerializedName("scheduledActions")
    private ScheduledAction[] scheduledActions;

    @SerializedName("targetTrackingPolicies")
    private TargetTrackingPolicy[] targetTrackingPolicies;

    @SerializedName("alwaysAllocateCPU")
    private boolean alwaysAllocateCPU;

    public ProvisionTarget(String resource, Integer target) {
        this.resource = resource;
        this.target = target;
    }

    public String getResource() {
        return resource;
    }

    public ProvisionTarget setResource(String resource) {
        this.resource = resource;
        return this;
    }

    public Integer getTarget() {
        return target;
    }

    public ProvisionTarget setTarget(Integer target) {
        this.target = target;
        return this;
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

    public boolean isAlwaysAllocateCPU() {
        return alwaysAllocateCPU;
    }

    public void setAlwaysAllocateCPU(boolean alwaysAllocateCPU) {
        this.alwaysAllocateCPU = alwaysAllocateCPU;
    }

    @Override
    public String toString() {
        return "ProvisionTarget{" +
                "resource='" + resource + '\'' +
                ", target=" + target +
                ", scheduledActions=" + Arrays.toString(scheduledActions) +
                ", targetTrackingPolicies=" + Arrays.toString(targetTrackingPolicies) +
                ", alwaysAllocateCPU=" + alwaysAllocateCPU +
                '}';
    }
}