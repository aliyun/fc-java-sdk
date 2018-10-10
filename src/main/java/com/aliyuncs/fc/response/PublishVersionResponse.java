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
import com.google.common.base.Preconditions;

/**
 * TODO: add javadoc
 */
public class PublishVersionResponse extends HttpResponse {

    private VersionMetaData versionMetadata;

    public PublishVersionResponse(VersionMetaData versionMetadata) {
        this.versionMetadata = versionMetadata;
    }

    public VersionMetaData getVersionMetadata() {
        return versionMetadata;
    }

    public PublishVersionResponse setVersionMetadata(VersionMetaData versionMetadata) {
        this.versionMetadata = versionMetadata;
        return this;
    }

    public Integer getVersionId() {
        Preconditions.checkArgument(versionMetadata != null);
        return versionMetadata.getVersionId();
    }

    public String getDescription() {
        Preconditions.checkArgument(versionMetadata != null);
        return versionMetadata.getDescription();
    }

    public String getCreatedTime() {
        Preconditions.checkArgument(versionMetadata != null);
        return versionMetadata.getCreatedTime();
    }

    public String getLastModifiedTime() {
        Preconditions.checkArgument(versionMetadata != null);
        return versionMetadata.getLastModifiedTime();
    }

}
