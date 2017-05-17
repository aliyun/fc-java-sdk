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
package com.aliyuncs.fc.exceptions;


import com.google.gson.annotations.SerializedName;
import java.util.Map;

/**
 * TODO: add javadoc
 */
public class ClientException extends RuntimeException {

    private static final long serialVersionUID = 534996425110290578L;

    private Map<String, String> headers;

    private int statusCode;

    @SerializedName("ErrorCode")
    private String errorCode;

    @SerializedName("ErrorMessage")
    private String errorMessage;

    private String requestId;

    public ClientException(String errCode, String errMsg, String requestId) {
        this(errCode, errMsg);
        this.requestId = requestId;
    }

    public ClientException(String errCode, String errMsg) {
        super(errCode + " : " + errMsg);
        this.errorCode = errCode;
        this.errorMessage = errMsg;
    }

    public ClientException(String errCode, String errMsg, Throwable cause) {
        super(errCode + " : " + errMsg, cause);
        this.errorCode = errCode;
        this.errorMessage = errMsg;
    }

    public ClientException(String message) {
        super(message);
    }

    public ClientException(Throwable cause) {
        super(cause);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public ClientException setStatusCode(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public ClientException setHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public ClientException setErrCode(String errCode) {
        this.errorCode = errCode;
        return this;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public ClientException setErrMsg(String errMsg) {
        this.errorMessage = errMsg;
        return this;
    }

    @Override
    public String getMessage() {
        if (requestId == null) {
            return super.getMessage();
        }
        return  "RequestId: " + requestId + ", ErrorCode: " +
            errorCode + ", ErrorMessage: " + errorMessage;
    }
}
