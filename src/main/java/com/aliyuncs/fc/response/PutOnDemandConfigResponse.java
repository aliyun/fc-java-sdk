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
import com.aliyuncs.fc.model.OnDemandConfigMetadata;

import com.google.common.base.Preconditions;
import java.util.Map;

/**
 * TODO: add javadoc
 */
public class PutOnDemandConfigResponse extends HttpResponse {

    private Map<String, String> header;
    private OnDemandConfigMetadata onDemandConfigMetadata;

    public Map<String, String> getHeader() {
        return header;
    }

    public PutOnDemandConfigResponse setHeader(Map<String, String> header) {
        this.header = header;
        return this;
    }

    public String getResource() {
        return onDemandConfigMetadata.getResource();
    }

    public void setOnDemandConfigMetadata(OnDemandConfigMetadata onDemandConfigMetadata) {
        this.onDemandConfigMetadata = onDemandConfigMetadata;
    }

    public int getMaximumInstanceCount() {
        return onDemandConfigMetadata.getMaximumInstanceCount();
    }

    public String getRequestId() {
        return header.get("X-Fc-Request-Id");
    }

    public String getEtag() {
        return header.get("Etag");
    }
}
