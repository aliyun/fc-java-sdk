package com.aliyuncs.fc.client;

public interface FcCallback<Req, Res> {
    void onCompleted(Req request, Res response);

    void onFailed(Req request, Exception exception);
}
