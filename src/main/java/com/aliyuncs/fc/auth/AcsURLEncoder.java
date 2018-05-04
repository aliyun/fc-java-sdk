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

import com.google.common.base.Strings;
import com.google.common.net.UrlEscapers;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * TODO: add javadoc
 */
public class AcsURLEncoder {

    public final static String URL_ENCODING = "UTF-8";

    /**
     * used for encoding url path segment
     */
    public static String urlEncode(String path) throws URISyntaxException {
        if (isNullOrEmpty(path)) return path;

        return UrlEscapers.urlFragmentEscaper().escape(path);
    }

    /**
     * used for encoding queries or form data
     *
     */
    public static String encode(String value) throws UnsupportedEncodingException {
        if (isNullOrEmpty(value)) return value;
        return URLEncoder.encode(value, URL_ENCODING);
    }

    public static String decode(String value) throws UnsupportedEncodingException {
        if (isNullOrEmpty(value)) return value;
        return URLDecoder.decode(value, URL_ENCODING);
    }

    public static String percentEncode(String value) throws UnsupportedEncodingException {
        return value != null ? URLEncoder.encode(value, URL_ENCODING).replace("+", "%20")
            .replace("*", "%2A").replace("%7E", "~") : null;
    }
}
