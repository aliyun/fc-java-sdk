package com.aliyuncs.fc.request;

import com.aliyuncs.fc.constants.Const;
import com.aliyuncs.fc.exceptions.ClientException;
import com.aliyuncs.fc.http.HttpRequest;
import com.aliyuncs.fc.model.Code;
import com.aliyuncs.fc.utils.ParameterHelper;
import com.google.common.base.Strings;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

import static com.aliyuncs.fc.constants.Const.LAYER_PATH;

public class PublishLayerVersionRequest extends HttpRequest {
    @SerializedName("layerName")
    private String layerName;

    @SerializedName("code")
    private Code code;

    @SerializedName("description")
    private String description;

    @SerializedName("compatibleRuntime")
    private String[] compatibleRuntime;

    @Override
    public String getPath() {
        return String.format(LAYER_PATH, Const.API_VERSION, this.layerName);
    }

    @Override
    public void validate() throws ClientException {
    }

    @Override
    public Map<String, String> getQueryParams() {
        return super.getQueryParams();
    }

    @Override
    public byte[] getPayload() {
        return ParameterHelper.ObjectToJson(this).getBytes();
    }

    public String getLayerName() {
        return layerName;
    }

    public void setLayerName(String layerName) {
        this.layerName = layerName;
    }

    public Code getCode() {
        return code;
    }

    public void setCode(Code code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getCompatibleRuntime() {
        return compatibleRuntime;
    }

    public void setCompatibleRuntime(String[] compatibleRuntime) {
        this.compatibleRuntime = compatibleRuntime;
    }
}
