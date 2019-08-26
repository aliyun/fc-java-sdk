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
import com.aliyuncs.fc.response.UntagResourceResponse;
import com.aliyuncs.fc.utils.ParameterHelper;
import com.google.common.base.Strings;
import com.google.gson.annotations.SerializedName;

public class UntagResourceRequest extends HttpRequest {
    @SerializedName("resourceArn")
    private String resourceArn;

    @SerializedName("tagKeys")
    private String[] tagKeys;

    @SerializedName("all")
    private boolean all;

    public UntagResourceRequest(){}

    public UntagResourceRequest(boolean all, String resourceArn, String[] tagKeys) {
        this.all = all;
        this.resourceArn = resourceArn;
        this.tagKeys = tagKeys;
    }

    public boolean isAll() {
        return all;
    }

    public void setAll(boolean all) {
        this.all = all;
    }

    public String getResourceArn() {
        return resourceArn;
    }

    public void setResourceArn(String resourceArn) {
        this.resourceArn = resourceArn;
    }

    public String[] getTagKeys() {
        return tagKeys;
    }

    public void setTagKeys(String[] tagKeys) {
        this.tagKeys = tagKeys;
    }

    public String getPath() {
        return String.format(Const.TAG_PATH, Const.API_VERSION);
    }

    public byte[] getPayload() {
        return ParameterHelper.ObjectToJson(this).getBytes();
    }

    public void validate() throws ClientException {
        if (Strings.isNullOrEmpty(resourceArn)) {
            throw new ClientException("resourceArn cannot be blank");
        }

        if(tagKeys.length == 0 && !all){
            throw new ClientException("At least 1 tag key is required if all=false");
        }
    }

    public Class<UntagResourceResponse> getResponseClass() {
        return UntagResourceResponse.class;
    }
}
