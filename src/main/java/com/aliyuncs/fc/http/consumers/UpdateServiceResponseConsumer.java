package com.aliyuncs.fc.http.consumers;

import com.aliyuncs.fc.http.HttpResponse;
import com.aliyuncs.fc.response.ResponseFactory;
import com.aliyuncs.fc.response.UpdateServiceResponse;


public class UpdateServiceResponseConsumer extends AbstractResponseConsumer<UpdateServiceResponse> {
    @Override
    protected UpdateServiceResponse parseResult() throws Exception {
        HttpResponse response = getFcHttpResponse();
        return ResponseFactory.genUpdateServiceResponse(response);
    }
}
