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
}

