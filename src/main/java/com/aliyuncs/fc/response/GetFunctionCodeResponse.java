package com.aliyuncs.fc.response;

import com.aliyuncs.fc.model.FunctionCodeMetadata;
import com.aliyuncs.fc.http.HttpResponse;

import com.google.common.base.Preconditions;
import java.util.Map;

/**
 * TODO: add javadoc
 */
public class GetFunctionCodeResponse extends HttpResponse {

    private Map<String, String> header;
    private FunctionCodeMetadata functionCodeMetadata;

    public void setFunctionCodeMetadata(FunctionCodeMetadata functionCodeMetadata) {
        this.functionCodeMetadata = functionCodeMetadata;
    }

    public FunctionCodeMetadata getFunctionCodeMetadata() {
        Preconditions.checkArgument(functionCodeMetadata != null);
        return functionCodeMetadata;
    }

    public String getCodeUrl() {
        Preconditions.checkArgument(functionCodeMetadata != null);
        return functionCodeMetadata.getUrl();
    }

    public String getCodeChecksum() {
        Preconditions.checkArgument(functionCodeMetadata != null);
        return functionCodeMetadata.getChecksum();
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public void setHeader(Map<String, String> header) {
        this.header = header;
    }

    public String getRequestId() {
        return header.get("X-Fc-Request-Id");
    }
}
