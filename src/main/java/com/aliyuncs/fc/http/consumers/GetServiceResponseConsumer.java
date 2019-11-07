package com.aliyuncs.fc.http.consumers;

import com.aliyuncs.fc.http.HttpResponse;
import com.aliyuncs.fc.response.GetServiceResponse;
import com.aliyuncs.fc.response.ResponseFactory;


public class GetServiceResponseConsumer extends AbstractResponseConsumer<GetServiceResponse> {
    @Override
    protected GetServiceResponse parseResult() throws Exception {
        HttpResponse response = getFcHttpResponse();
        return ResponseFactory.genGetServiceResponse(response);
    }
}
