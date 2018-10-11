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
package com.aliyuncs.fc.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains helper methods to build url query string parameters
 */
public class ListRequestUrlHelper {

    public static Map<String, String> buildParams(String prefix, String startKey,
        String nextToken, Integer limit) {
        Map<String, String> queryParams = new HashMap<String, String>();
        if (prefix != null) {
            queryParams.put("prefix", prefix);
        }

        if (startKey != null) {
            queryParams.put("startKey", startKey);
        }

        if (nextToken != null) {
            queryParams.put("nextToken", nextToken);
        }

        if (limit != null) {
            queryParams.put("limit", limit.toString());
        }

        return queryParams;
    }

    public static Map<String, String> buildListVersionParams(String startKey,
        String nextToken, Integer limit, String direction) {
        Map<String, String> queryParams = new HashMap<String, String>();

        if (startKey != null) {
            queryParams.put("startKey", startKey);
        }

        if (nextToken != null) {
            queryParams.put("nextToken", nextToken);
        }

        if (limit != null) {
            queryParams.put("limit", limit.toString());
        }

        if (direction != null) {
            queryParams.put("direction", direction.toString());
        }
        return queryParams;
    }
}

