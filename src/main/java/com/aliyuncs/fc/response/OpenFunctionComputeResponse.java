package com.aliyuncs.fc.response;

import com.aliyuncs.fc.http.HttpResponse;

public class OpenFunctionComputeResponse extends HttpResponse {

    private String requestId;

    private String orderId;

    private String code;

    private String msg;

    @Override
    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "OpenFunctionComputeResponse{" +
                "requestId='" + requestId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
