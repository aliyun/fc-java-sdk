package com.aliyuncs.fc.request;

import com.aliyuncs.fc.constants.Const;
import com.aliyuncs.fc.exceptions.ClientException;
import com.aliyuncs.fc.http.HttpRequest;
import com.google.common.base.Strings;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

import static com.aliyuncs.fc.constants.Const.LAYER_VERSION_PATH;

public class DeleteLayerVersionRequest extends HttpRequest {

    @SerializedName("layerName")
    private String layerName;

    @SerializedName("version")
    private String version;

    @Override
    public String getPath() {
        return String.format(LAYER_VERSION_PATH, Const.API_VERSION, this.layerName, this.version);
    }

    @Override
    public void validate() throws ClientException {
    }

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
}
