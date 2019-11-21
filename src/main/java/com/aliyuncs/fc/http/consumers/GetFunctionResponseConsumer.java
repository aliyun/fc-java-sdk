package com.aliyuncs.fc.http.consumers;

import com.aliyuncs.fc.http.HttpResponse;
import com.aliyuncs.fc.response.GetFunctionResponse;
import com.aliyuncs.fc.response.ResponseFactory;


public class GetFunctionResponseConsumer extends AbstractResponseConsumer<GetFunctionResponse> {
    @Override
    protected GetFunctionResponse parseResult() throws Exception {
        HttpResponse response = getFcHttpResponse();
        return ResponseFactory.genGetFunctionResponse(response);
    }
}
