package com.aliyuncs.fc.http.consumers;

import com.aliyuncs.fc.http.HttpResponse;
import com.aliyuncs.fc.response.ResponseFactory;
import com.aliyuncs.fc.response.UpdateFunctionResponse;


public class UpdateFunctionResponseConsumer extends AbstractResponseConsumer<UpdateFunctionResponse> {
    @Override
    protected UpdateFunctionResponse parseResult() throws Exception {
        HttpResponse response = getFcHttpResponse();
        return ResponseFactory.genUpdateFunctionResponse(response);
    }
}
