package com.aliyuncs.fc.http.consumers;

import com.aliyuncs.fc.http.HttpResponse;
import com.aliyuncs.fc.response.CreateServiceResponse;
import com.aliyuncs.fc.response.ResponseFactory;


public class CreateServiceResponseConsumer extends AbstractResponseConsumer<CreateServiceResponse> {
    @Override
    protected CreateServiceResponse parseResult() throws Exception {
        HttpResponse response = getFcHttpResponse();
        return ResponseFactory.genCreateServiceResponse(response);
    }
}
