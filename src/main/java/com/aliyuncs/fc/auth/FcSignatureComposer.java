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
package com.aliyuncs.fc.auth;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.aliyuncs.fc.utils.Base64Helper;
import com.aliyuncs.fc.utils.ParameterHelper;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * TODO: add javadoc
 */
public class FcSignatureComposer {

    private static FcSignatureComposer composer = null;

    protected final static String HEADER_SEPARATOR = "\n";
    private final static String AGLORITHM_NAME = "HmacSHA256";

    public static Map<String, String> refreshSignParameters(Map<String, String> parameters) {
        if (parameters == null) {
            parameters = new HashMap<String, String>();
        }
        parameters.put("Date", ParameterHelper.getRFC2616Date(null));
        return parameters;
    }

    public static String composeStringToSign(String method, String path,
        Map<String, String> headers) {
        StringBuilder sb = new StringBuilder();
        sb.append(method).append(HEADER_SEPARATOR);
        if (headers.get("Content-MD5") != null) {
            sb.append(headers.get("Content-MD5"));
        }
        sb.append(HEADER_SEPARATOR);
        if (headers.get("Content-Type") != null) {
            sb.append(headers.get("Content-Type"));
        }
        sb.append(HEADER_SEPARATOR);
        if (headers.get("Date") != null) {
            sb.append(headers.get("Date"));
        }
        sb.append(HEADER_SEPARATOR);
        sb.append(buildCanonicalHeaders(headers, "x-fc"));
        sb.append(path);
        return sb.toString();
    }

    public static String buildCanonicalHeaders(Map<String, String> headers, String headerBegin) {
        Map<String, String> sortMap = new TreeMap<String, String>();
        for (Map.Entry<String, String> e : headers.entrySet()) {
            String key = e.getKey().toLowerCase();
            String val = e.getValue();
            if (key.startsWith(headerBegin)) {
                sortMap.put(key, val);
            }
        }
        StringBuilder headerBuilder = new StringBuilder();
        for (Map.Entry<String, String> e : sortMap.entrySet()) {
            headerBuilder.append(e.getKey());
            headerBuilder.append(':').append(e.getValue());
            headerBuilder.append(HEADER_SEPARATOR);
        }
        return headerBuilder.toString();
    }

    public static String signString(String source, String accessSecret)
        throws InvalidKeyException, IllegalStateException {
        try {
            Mac mac = Mac.getInstance(AGLORITHM_NAME);
            mac.init(new SecretKeySpec(
                accessSecret.getBytes(AcsURLEncoder.URL_ENCODING), AGLORITHM_NAME));
            byte[] signData = mac.doFinal(source.getBytes(AcsURLEncoder.URL_ENCODING));
            return Base64Helper.encode(signData);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("HMAC-SHA1 not supported.");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 not supported.");
        }

    }

    public String getSignerName() {
        return "HMAC-SHA256";
    }

    public String getSignerVersion() {
        return "1.0";
    }

    public static FcSignatureComposer getComposer() {
        if (null == composer) {
            composer = new FcSignatureComposer();
        }
        return composer;
    }

}
