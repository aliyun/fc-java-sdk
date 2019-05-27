package com.aliyuncs.fc.utils;


import com.aliyuncs.fc.constants.Const;
import com.aliyuncs.fc.exceptions.ClientException;

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
}
