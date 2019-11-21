package com.aliyuncs.fc.http.consumers;

import com.aliyuncs.fc.http.HttpResponse;
import com.aliyuncs.fc.response.CreateFunctionResponse;
import com.aliyuncs.fc.response.ResponseFactory;


public class CreateFunctionResponseConsumer extends AbstractResponseConsumer<CreateFunctionResponse> {
    @Override
    protected CreateFunctionResponse parseResult() throws Exception {
        HttpResponse response = getFcHttpResponse();
        return ResponseFactory.genCreateFunctionResponse(response);
    }
}
