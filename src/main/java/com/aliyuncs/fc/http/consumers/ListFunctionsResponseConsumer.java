package com.aliyuncs.fc.http.consumers;

import com.aliyuncs.fc.http.HttpResponse;
import com.aliyuncs.fc.response.ListFunctionsResponse;
import com.aliyuncs.fc.response.ResponseFactory;


public class ListFunctionsResponseConsumer extends AbstractResponseConsumer<ListFunctionsResponse> {
    @Override
    protected ListFunctionsResponse parseResult() throws Exception {
        HttpResponse response = getFcHttpResponse();
        return ResponseFactory.genListFunctionResponse(response);
    }
}
