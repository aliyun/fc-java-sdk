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

import com.aliyuncs.fc.constants.Const;
import com.aliyuncs.fc.exceptions.ClientException;
import com.aliyuncs.fc.http.HttpRequest;
import com.aliyuncs.fc.response.TagResourceResponse;
import com.aliyuncs.fc.utils.ParameterHelper;
import com.google.common.base.Strings;
import com.google.gson.annotations.SerializedName;
import java.util.Map;

public class TagResourceRequest extends HttpRequest {

    @SerializedName("resourceArn")
    private String resourceArn;

    @SerializedName("tags")
    private Map<String, String> tags;

    public TagResourceRequest(){}

    public TagResourceRequest(String resourceArn, Map<String, String> tags) {
        this.resourceArn = resourceArn;
        this.tags = tags;
    }

    public String getResourceArn() {
        return resourceArn;
    }

    public void setResourceArn(String resourceArn) {
        this.resourceArn = resourceArn;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public void setTags(Map<String, String> tags) {
        this.tags = tags;
    }
    public byte[] getPayload() {
        return ParameterHelper.ObjectToJson(this).getBytes();
    }

    public String getPath() {
        return String.format(Const.TAG_PATH, Const.API_VERSION);
    }

    public void validate() throws ClientException {
        if (Strings.isNullOrEmpty(resourceArn)) {
            throw new ClientException("resourceArn cannot be blank");
        }
        if (tags.isEmpty()) {
            throw new ClientException("tags cannot be empty");
        }
    }

    public Class<TagResourceResponse> getResponseClass() {
        return TagResourceResponse.class;
    }
}
