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

import com.aliyuncs.fc.model.FunctionMetadata;
import com.aliyuncs.fc.http.HttpResponse;

import com.google.common.base.Preconditions;
import java.util.Map;

/**
 * TODO: add javadoc
 */
public class UpdateFunctionResponse extends HttpResponse {

    private FunctionMetadata functionMetadata;

    public FunctionMetadata getFunctionMetadata() {
        return functionMetadata;
    }

    public UpdateFunctionResponse setFunctionMetadata(FunctionMetadata functionMetadata) {
        this.functionMetadata = functionMetadata;
        return this;
    }

    public String getFunctionName() {
        Preconditions.checkArgument(functionMetadata != null);
        return functionMetadata.getFunctionName();
    }

    public String getFunctionId() {
        Preconditions.checkArgument(functionMetadata != null);
        return functionMetadata.getFunctionId();
    }

    public String getDescription() {
        Preconditions.checkArgument(functionMetadata != null);
        return functionMetadata.getDescription();
    }

    public String getRuntime() {
        Preconditions.checkArgument(functionMetadata != null);
        return functionMetadata.getRuntime();
    }

    public String getHandler() {
        Preconditions.checkArgument(functionMetadata != null);
        return functionMetadata.getHandler();
    }

    public Integer getTimeout() {
        Preconditions.checkArgument(functionMetadata != null);
        return functionMetadata.getTimeout();
    }

    public Integer getMemorySize() {
        Preconditions.checkArgument(functionMetadata != null);
        return functionMetadata.getMemorySize();
    }

    public int getCodeSize() {
        Preconditions.checkArgument(functionMetadata != null);
        return functionMetadata.getCodeSize();
    }

    public String getCodeChecksum() {
        Preconditions.checkArgument(functionMetadata != null);
        return functionMetadata.getCodeChecksum();
    }

    public String getCreatedTime() {
        Preconditions.checkArgument(functionMetadata != null);
        return functionMetadata.getCreatedTime();
    }

    public String getLastModifiedTime() {
        Preconditions.checkArgument(functionMetadata != null);
        return functionMetadata.getLastModifiedTime();
    }

    public Map<String, String> getEnvironmentVariables() {
        Preconditions.checkArgument(functionMetadata != null);
        return functionMetadata.getEnvironmentVariables();
    }
}
