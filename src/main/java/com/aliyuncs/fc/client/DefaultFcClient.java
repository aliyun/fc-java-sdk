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
 *
 */
package com.aliyuncs.fc.client;

import static com.aliyuncs.fc.model.HttpAuthType.ANONYMOUS;

import com.aliyuncs.fc.config.Config;
import com.aliyuncs.fc.constants.HeaderKeys;
import com.aliyuncs.fc.exceptions.ClientException;
import com.aliyuncs.fc.exceptions.ServerException;
import com.aliyuncs.fc.http.HttpRequest;
import com.aliyuncs.fc.http.HttpResponse;
import com.aliyuncs.fc.model.HttpMethod;
import com.aliyuncs.fc.model.PrepareUrl;
import com.aliyuncs.fc.request.HttpInvokeFunctionRequest;
import com.aliyuncs.fc.utils.FcUtil;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;


import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


public class DefaultFcClient {

    public final static Boolean AUTO_RETRY = true;
    public final static int MAX_RETRIES = 3;
    private final Config config;

    public DefaultFcClient(Config config) {
        this.config = config;
    }

    /**
     * if form paramter is null, it will use content-type of request.headers
     */
    public HttpResponse doAction(HttpRequest request, String form, HttpMethod method)
        throws ClientException, ServerException {
        request.validate();
        
        // try refresh credentials if CredentialProvider set
        this.config.refreshCredentials();
        
        try {
            int retryTimes = 0;
            HttpResponse response = null;

            boolean httpInvoke = false;
            if (request instanceof HttpInvokeFunctionRequest) {
                httpInvoke = true;
            }

            do {
                if (!httpInvoke
                    || !ANONYMOUS.equals(((HttpInvokeFunctionRequest) request).getAuthType())) {
                    FcUtil.signRequest(config, request, form, method, httpInvoke);
                }

                PrepareUrl prepareUrl = FcUtil.prepareUrl(request.getPath(), request.getQueryParams(), config);

                response = HttpResponse.getResponse(prepareUrl.getUrl(), request, method,
                    config.getConnectTimeoutMillis(), config.getReadTimeoutMillis());

                retryTimes++;

                if (httpInvoke) {
                    return response;
                }

            } while (500 <= response.getStatus() && AUTO_RETRY && retryTimes < MAX_RETRIES);

            if (response.getStatus() >= 500) {
                String requestId = response.getHeader(HeaderKeys.REQUEST_ID);
                String stringContent =
                    response.getContent() == null ? "" : FcUtil.toDefaultCharset(response.getContent());
                ServerException se;
                try {
                    se = new Gson().fromJson(stringContent, ServerException.class);
                } catch (JsonParseException e) {
                    se = new ServerException("InternalServiceError",
                        "Failed to parse response content", requestId);
                }
                se.setStatusCode(response.getStatus());
                se.setRequestId(requestId);
                throw se;
            } else if (response.getStatus() >= 300) {
                ClientException ce;
                if (response.getContent() == null) {
                    ce = new ClientException("SDK.ServerUnreachable",
                        "Failed to get response content from server");
                } else {
                    try {
                        ce = new Gson()
                            .fromJson(FcUtil.toDefaultCharset(response.getContent()), ClientException.class);
                    } catch (JsonParseException e) {
                        ce = new ClientException("SDK.ResponseNotParsable",
                            "Failed to parse response content", e);
                    }
                }
                if (ce == null) {
                    ce = new ClientException("SDK.UnknownError", "Unknown client error");
                }
                ce.setStatusCode(response.getStatus());
                ce.setRequestId(response.getHeader(HeaderKeys.REQUEST_ID));
                throw ce;
            }
            return response;
        } catch (InvalidKeyException exp) {
            throw new ClientException("SDK.InvalidAccessSecret",
                "Speicified access secret is not valid.");
        } catch (SocketTimeoutException exp) {
            throw new ClientException("SDK.ServerUnreachable",
                "SocketTimeoutException has occurred on a socket read or accept.");
        } catch (IOException exp) {
            throw new ClientException("SDK.ServerUnreachable",
                "Server unreachable: " + exp.toString());
        } catch (NoSuchAlgorithmException exp) {
            throw new ClientException("SDK.InvalidMD5Algorithm",
                "MD5 hash is not supported by client side.");
        } catch (URISyntaxException e) {
            throw new ClientException("SDK.InvalidURL", "url is not valid: " + e.getMessage());
        }
    }
}
