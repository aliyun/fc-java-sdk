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
import com.aliyuncs.fc.model.VersionMetaData;
import com.google.gson.annotations.SerializedName;

public class ListVersionsResponse extends HttpResponse {

    @SerializedName("versions")
    private VersionMetaData[] versions;

    @SerializedName("nextToken")
    private String nextToken;

    @SerializedName("direction")
    private String direction;

    public VersionMetaData[] getVersions() {
        return versions;
    }

    public ListVersionsResponse(VersionMetaData[] versions) {
        this.versions = versions;
    }

    public String getNextToken() {
        return nextToken;
    }

    public ListVersionsResponse setNextToken(String nextToken) {
        this.nextToken = nextToken;
        return this;
    }

    public String getDirection() {
        return direction;
    }

    public ListVersionsResponse setDirection(String direction) {
        this.direction = direction;
        return this;
    }
}
