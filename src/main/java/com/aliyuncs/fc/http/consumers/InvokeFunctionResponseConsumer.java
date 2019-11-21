package com.aliyuncs.fc.http.consumers;

import com.aliyuncs.fc.http.HttpResponse;
import com.aliyuncs.fc.response.InvokeFunctionResponse;
import com.aliyuncs.fc.response.ResponseFactory;


public class InvokeFunctionResponseConsumer extends AbstractResponseConsumer<InvokeFunctionResponse> {
    @Override
    protected InvokeFunctionResponse parseResult() throws Exception {
        HttpResponse response = getFcHttpResponse();
        return ResponseFactory.genInvokeFunctionResponse(response);
    }
}
