package com.aliyuncs.fc.http.consumers;

import com.aliyuncs.fc.http.HttpResponse;
import com.aliyuncs.fc.response.DeleteFunctionResponse;
import com.aliyuncs.fc.response.ResponseFactory;


public class DeleteFunctionResponseConsumer extends AbstractResponseConsumer<DeleteFunctionResponse> {
    @Override
    protected DeleteFunctionResponse parseResult() throws Exception {
        HttpResponse response = getFcHttpResponse();
        return ResponseFactory.genDeleteFunctionResponse(response);
    }
}
