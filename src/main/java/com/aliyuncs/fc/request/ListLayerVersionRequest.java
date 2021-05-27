package com.aliyuncs.fc.request;

import com.aliyuncs.fc.constants.Const;
import com.aliyuncs.fc.exceptions.ClientException;
import com.aliyuncs.fc.http.HttpRequest;
import com.google.common.base.Strings;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

import static com.aliyuncs.fc.constants.Const.LAYER_PATH;

public class ListLayerVersionRequest extends HttpRequest {

    @SerializedName("layerName")
    private String layerName;

    @SerializedName("startVersion")
    private String startVersion;

    @SerializedName("limit")
    private Integer limit;

    @Override
    public String getPath() {
        return String.format(LAYER_PATH, Const.API_VERSION, this.layerName);
    }

    @Override
    public void validate() throws ClientException {
    }

    @Override
    public Map<String, String> getQueryParams() {
        Map<String, String> queryParams = new HashMap<String, String>();
        if (StringUtils.isNotBlank(layerName)) {
            queryParams.put("layerName", layerName);
        }
        if (StringUtils.isNotBlank(startVersion)) {
            queryParams.put("startVersion", startVersion);
        }
        if (limit != null) {
            queryParams.put("limit", String.valueOf(limit));
        }
        return queryParams;
    }

    public String getLayerName() {
        return layerName;
    }

    public void setLayerName(String layerName) {
        this.layerName = layerName;
    }

    public String getStartVersion() {
        return startVersion;
    }

    public void setStartVersion(String startVersion) {
        this.startVersion = startVersion;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
