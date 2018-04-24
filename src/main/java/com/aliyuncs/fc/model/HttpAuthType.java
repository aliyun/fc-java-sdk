package com.aliyuncs.fc.model;

import com.google.gson.annotations.SerializedName;

public enum HttpAuthType {

    /**
     * Invoke an FC function without authorization.
     */
    @SerializedName("anonymous")
    ANONYMOUS,

    /**
     * Invoke an FC function via authorization with Aliyun AK signed.
     *
     * link https://help.aliyun.com/document_detail/53252.html
     */
    @SerializedName("function")
    FUNCTION
}
