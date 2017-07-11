package com.aliyuncs.fc.request;

import com.aliyuncs.fc.constants.Const;

/**
 * TODO: add javadoc
 */
public class GetFunctionCodeRequest extends GetFunctionRequest {

    public GetFunctionCodeRequest(String serviceName, String functionName) {
        super(serviceName, functionName);
    }

    public String getPath() {
        return String.format(Const.FUNCTION_CODE_PATH, Const.API_VERSION, getServiceName(),
                getFunctionName());
    }
}
