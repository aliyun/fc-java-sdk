package com.aliyuncs.fc.http.consumers;

import com.aliyuncs.fc.http.HttpResponse;
import com.aliyuncs.fc.response.DeleteServiceResponse;
import com.aliyuncs.fc.response.ResponseFactory;


public class DeleteServiceResponseConsumer extends AbstractResponseConsumer<DeleteServiceResponse> {
    @Override
    protected DeleteServiceResponse parseResult() throws Exception {
        HttpResponse response = getFcHttpResponse();
        return ResponseFactory.genDeleteServiceResponse(response);
    }
}
