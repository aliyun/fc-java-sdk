package com.aliyuncs.fc.response;

import com.aliyuncs.fc.http.HttpResponse;
import com.aliyuncs.fc.model.Code;

public class GetLayerVersionResponse extends HttpResponse {

    private String layerName;
    private String version;
    private Code code;
    private String codeSha256;
    private Integer codeSize;
    private String location;

    public String getLayerName() {
        return layerName;
    }

    public void setLayerName(String layerName) {
        this.layerName = layerName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Code getCode() {
        return code;
    }

    public void setCode(Code code) {
        this.code = code;
    }

    public String getCodeSha256() {
        return codeSha256;
    }

    public void setCodeSha256(String codeSha256) {
        this.codeSha256 = codeSha256;
    }

    public Integer getCodeSize() {
        return codeSize;
    }

    public void setCodeSize(Integer codeSize) {
        this.codeSize = codeSize;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
