package com.aliyuncs.fc.request;

import com.aliyuncs.fc.constants.Const;
import com.aliyuncs.fc.exceptions.ClientException;
import com.aliyuncs.fc.http.HttpRequest;
import com.google.common.base.Strings;
import com.google.gson.annotations.SerializedName;

import static com.aliyuncs.fc.constants.Const.LAYER_VERSION_PATH;

public class GetLayerVersionRequest extends HttpRequest {

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
        if (Strings.isNullOrEmpty(layerName)) {
            throw new ClientException("LayerName cannot be blank");
        }
        if (Strings.isNullOrEmpty(version)) {
            throw new ClientException("Version cannot be blank");
        }
    }
}
