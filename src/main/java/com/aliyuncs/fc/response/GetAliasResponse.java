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
import com.aliyuncs.fc.model.AliasMetaData;
import com.google.common.base.Preconditions;
import java.util.Map;

public class GetAliasResponse extends HttpResponse {

    private AliasMetaData aliasMetaData;

    public GetAliasResponse(AliasMetaData aliasMetaData) {
        this.aliasMetaData = aliasMetaData;
    }

    public AliasMetaData getAliasMetaData() {
        return aliasMetaData;
    }

    public GetAliasResponse setAliasMetaData(AliasMetaData aliasMetaData) {
        this.aliasMetaData = aliasMetaData;
        return this;
    }

    public String getDescription() {
        Preconditions.checkArgument(aliasMetaData != null);
        return aliasMetaData.getDescription();
    }

    public Integer getVersionId() {
        Preconditions.checkArgument(aliasMetaData != null);
        return aliasMetaData.getVersionId();
    }

    public String getAliasName() {
        Preconditions.checkArgument(aliasMetaData != null);
        return aliasMetaData.getAliasName();
    }

    public Map<Integer, Float> getAdditionalVersionWeight() {
        Preconditions.checkArgument(aliasMetaData != null);
        return aliasMetaData.getAdditionalVersionWeight();
    }
}
