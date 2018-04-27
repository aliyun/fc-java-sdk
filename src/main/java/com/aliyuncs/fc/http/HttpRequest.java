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
package com.aliyuncs.fc.http;

import com.aliyuncs.fc.exceptions.ClientException;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.aliyuncs.fc.constants.Const;

public abstract class HttpRequest {

    protected Map<String, String> headers;
    public abstract String getPath();
    public abstract Map<String, String> getQueryParams();
    public abstract byte[] getPayload();
    public abstract void validate() throws ClientException;

    public HttpRequest() {
        headers = new HashMap<String, String>();
    }

    public HttpURLConnection getHttpConnection(String urls, String method)
        throws IOException {
        String strUrl = urls;
        if (null == strUrl || null == method) {
            return null;
        }
        URL url = new URL(strUrl);

        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestMethod(method);
        httpConn.setDoOutput(true);
        httpConn.setDoInput(true);
        httpConn.setUseCaches(false);
        httpConn.setConnectTimeout(Const.CONNECT_TIMEOUT);
        httpConn.setReadTimeout(Const.READ_TIMEOUT);

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            httpConn.setRequestProperty(entry.getKey(), entry.getValue());
        }

        return httpConn;
    }

    public void setHeader(String key, String value) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(key), "Header key cannot be blank");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(value), "Header value cannot be blank");
        headers.put(key, value);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }
}
