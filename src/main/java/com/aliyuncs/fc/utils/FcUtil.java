package com.aliyuncs.fc.utils;


import com.aliyuncs.fc.auth.FcSignatureComposer;
import com.aliyuncs.fc.config.Config;
import com.aliyuncs.fc.constants.Const;
import com.aliyuncs.fc.exceptions.ClientException;
import com.aliyuncs.fc.http.HttpRequest;
import com.aliyuncs.fc.model.HttpMethod;
import com.aliyuncs.fc.model.PrepareUrl;
import com.google.common.base.Preconditions;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import static com.aliyuncs.fc.auth.AcsURLEncoder.encode;
import static com.aliyuncs.fc.auth.AcsURLEncoder.urlEncode;
import static com.aliyuncs.fc.auth.FcSignatureComposer.composeStringToSign;
import static com.google.common.base.Strings.isNullOrEmpty;

public class FcUtil {
    public static String toDefaultCharset(byte[] content) throws ClientException {
        return toCharset(content, Const.DEFAULT_CHARSET);
    }
    
    public static String toCharset(byte[] content, String charsetName) throws ClientException {
        try {
            return new String(content, charsetName);
        } catch (Exception e) {
            throw new ClientException("SDK.DecodeContentError", e.getMessage());
        }
    }

    public static PrepareUrl prepareUrl(String path, Map<String, String> queryParams, Config config)
            throws UnsupportedEncodingException, URISyntaxException {
        return new PrepareUrl(composeUrl(config.getEndpoint() + urlEncode(path), queryParams));
    }

    public static String composeUrl(String endpoint, Map<String, String> queries)
            throws UnsupportedEncodingException {

        Map<String, String> mapQueries = queries;
        StringBuilder urlBuilder = new StringBuilder("");
        urlBuilder.append(endpoint);
        if (-1 == urlBuilder.indexOf("?")) {
            urlBuilder.append("?");
        } else if (!urlBuilder.toString().endsWith("?")) {
            urlBuilder.append("&");
        }
        String url = urlBuilder.toString();
        if (queries != null && queries.size() > 0) {
            String query = concatQueryString(mapQueries);
            url = urlBuilder.append(query).toString();
        }
        if (url.endsWith("?") || url.endsWith("&")) {
            url = url.substring(0, url.length() - 1);
        }
        return url;
    }

    /**
     * concate query string parameters (e.g. name=foo)
     *
     * @param parameters query parameters
     * @return concatenated query string
     * @throws UnsupportedEncodingException exceptions
     */
    public static String concatQueryString(Map<String, String> parameters)
            throws UnsupportedEncodingException {
        if (null == parameters) {
            return null;
        }

        StringBuilder urlBuilder = new StringBuilder("");
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            String key = entry.getKey();
            String val = entry.getValue();
            urlBuilder.append(encode(key));
            if (val != null) {
                urlBuilder.append("=").append(encode(val));
            }
            urlBuilder.append("&");
        }

        int strIndex = urlBuilder.length();
        if (parameters.size() > 0) {
            urlBuilder.deleteCharAt(strIndex - 1);
        }

        return urlBuilder.toString();
    }

    public static Map<String, String> getHeader(Config config, Map<String, String> header, byte[] payload, String form) {
        if (header == null) {
            header = new HashMap<String, String>();
        }
        header.put("User-Agent", config.getUserAgent());
        header.put("Accept", "application/json");
        header.put("Content-Type", form);
        header.put("x-fc-account-id", config.getUid());

        if (header.get("x-fc-date") == null
                && payload != null) { // x-fc-date is used for fc-console
            header.put("Content-MD5", ParameterHelper.md5Sum(payload));
        }

        if (!isNullOrEmpty(config.getSecurityToken())) {
            header.put("x-fc-security-token", config.getSecurityToken());
        }
        return header;
    }

    public static void signRequest(Config config, HttpRequest request, String form, HttpMethod method,
                            boolean includeParameters)
            throws InvalidKeyException, IllegalStateException, UnsupportedEncodingException, NoSuchAlgorithmException {

        Map<String, String> imutableMap = null;
        if (request.getHeaders() != null) {
            imutableMap = request.getHeaders();
        } else {
            imutableMap = new HashMap<String, String>();
        }
        String accessKeyId = config.getAccessKeyID();
        String accessSecret = config.getAccessKeySecret();

        Preconditions.checkArgument(!isNullOrEmpty(accessKeyId), "Access key cannot be blank");
        Preconditions.checkArgument(!isNullOrEmpty(accessSecret), "Secret key cannot be blank");
        imutableMap = FcSignatureComposer.refreshSignParameters(imutableMap);

        // Get relevant path
        String uri = request.getPath();

        // Set all headers
        imutableMap = getHeader(config, imutableMap, request.getPayload(), form);

        // Sign URL
        String strToSign = null;

        if (includeParameters) {
            strToSign = composeStringToSign(method, uri, imutableMap, request.getQueryParams());
        } else {
            strToSign = composeStringToSign(method, uri, imutableMap, null);
        }

        String signature = FcSignatureComposer.signString(strToSign, accessSecret);

        // Set signature
        imutableMap.put("Authorization", "FC " + accessKeyId + ":" + signature);
    }
}
