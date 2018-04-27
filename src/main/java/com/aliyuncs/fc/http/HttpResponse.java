/*
Z * Licensed to the Apache Software Foundation (ASF) under one
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

import com.aliyuncs.fc.model.HttpMethod;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class HttpResponse {

    private int status;
    private byte[] content;
    private Map<String, String> headers;
    public HttpResponse() {
    }

    public void setHeader(String key, String value) {
        if (headers == null) {
            headers = new HashMap<String, String>();
        }
        headers.put(key, value);
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public byte[] getContent() {
        return this.content;
    }

    public String getHeader(String name) {
        String value = this.headers.get(name);
        if (null == value) {
            value = this.headers.get(name.toLowerCase());
        }
        return value;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    private static byte[] readContent(InputStream content)
        throws IOException {

        if (content == null) {
            return null;
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];

        while (true) {
            final int read = content.read(buff);
            if (read == -1) {
                break;
            }
            outputStream.write(buff, 0, read);
        }

        return outputStream.toByteArray();
    }

    private static void parseHttpConn(HttpResponse response, HttpURLConnection httpConn,
        InputStream content) throws IOException {
        byte[] buff = readContent(content);
        response.setStatus(httpConn.getResponseCode());
        Map<String, List<String>> headers = httpConn.getHeaderFields();
        for (Entry<String, List<String>> entry : headers.entrySet()) {
            String key = entry.getKey();
            if (null == key) {
                continue;
            }
            List<String> values = entry.getValue();
            StringBuilder builder = new StringBuilder(values.get(0));
            for (int i = 1; i < values.size(); i++) {
                builder.append(",");
                builder.append(values.get(i));
            }
            response.setHeader(key, builder.toString());
        }

        response.setContent(buff);
    }

    // Get http response
    public static HttpResponse getResponse(String urls, HttpRequest request,
                                           HttpMethod method, int connectTimeoutMillis, int readTimeoutMillis) throws IOException {
        OutputStream out = null;
        InputStream content = null;
        HttpResponse response = null;
        HttpURLConnection httpConn = request
            .getHttpConnection(urls, method.name());
        httpConn.setConnectTimeout(connectTimeoutMillis);
        httpConn.setReadTimeout(readTimeoutMillis);

        try {
            httpConn.connect();
            if (null != request.getPayload() && request.getPayload().length > 0) {
                out = httpConn.getOutputStream();
                out.write(request.getPayload());
            }
            content = httpConn.getInputStream();
            response = new HttpResponse();
            parseHttpConn(response, httpConn, content);
            return response;
        } catch (SocketTimeoutException e) {
            throw e;
        } catch (IOException e) {
            content = httpConn.getErrorStream();
            response = new HttpResponse();
            parseHttpConn(response, httpConn, content);
            return response;
        } finally {
            if (content != null) {
                content.close();
            }
            httpConn.disconnect();
        }
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isSuccess() {
        if (200 <= this.status &&
            300 > this.status) {
            return true;
        }
        return false;
    }

    public String getRequestId() {
        return this.headers.get("X-Fc-Request-Id");
    }

    public String getEtag() {
        return this.headers.get("Etag");
    }
}
