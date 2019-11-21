package com.aliyuncs.fc.http.consumers;

import com.aliyuncs.fc.http.HttpResponse;
import com.aliyuncs.fc.response.ListServicesResponse;
import com.aliyuncs.fc.response.ResponseFactory;


public class ListServicesResponseConsumer extends AbstractResponseConsumer<ListServicesResponse> {
    @Override
    protected ListServicesResponse parseResult() throws Exception {
        HttpResponse response = getFcHttpResponse();
        return ResponseFactory.genListServiceResponse(response);
    }
}
