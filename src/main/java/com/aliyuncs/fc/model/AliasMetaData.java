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
import java.util.Map;

public class AliasMetaData {
    @SerializedName("aliasName")
    private String aliasName;

    @SerializedName("description")
    private String description;

    @SerializedName("versionId")
    private String versionId;

    @SerializedName("additionalVersionWeight")
    private Map<String, Float> additionalVersionWeight;

    public AliasMetaData(String description, String aliasName, String versionId,
        Map<String, Float> additionalVersionWeight) {
        this.description = description;
        this.versionId = versionId;
        this.aliasName = aliasName;
        this.additionalVersionWeight = additionalVersionWeight;
    }

    public String getAliasName() {
        return aliasName;
    }

    public AliasMetaData setAliasName(String aliasName) {
        this.aliasName = aliasName;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public AliasMetaData setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getVersionId() {
        return versionId;
    }

    public AliasMetaData setVersionId(String versionId) {
        this.versionId = versionId;
        return this;
    }

    public Map<String, Float> getAdditionalVersionWeight() {
        return additionalVersionWeight;
    }

    public AliasMetaData setAdditionalVersionWeight(
        Map<String, Float> additionalVersionWeight) {
        this.additionalVersionWeight = additionalVersionWeight;
        return this;
    }
}
