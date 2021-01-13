package com.aliyuncs.fc.request;

import com.aliyuncs.fc.constants.Const;
import com.aliyuncs.fc.exceptions.ClientException;
import com.aliyuncs.fc.http.HttpRequest;
import com.google.common.base.Strings;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

import static com.aliyuncs.fc.constants.Const.LAYERS_PATH;

public class ListLayerRequest extends HttpRequest {

    @SerializedName("prefix")
    private String prefix;

    @SerializedName("startKey")
    private String startKey;

    @SerializedName("nextToken")
    private String nextToken;

    @SerializedName("limit")
    private String limit;

    @SerializedName("isPublic")
    private Boolean isPublic;

    @Override
    public String getPath() {
        return String.format(LAYERS_PATH, Const.API_VERSION);
    }

    @Override
    public void validate() throws ClientException {
    }

    @Override
    public Map<String, String> getQueryParams() {
        Map<String, String> queryParams = new HashMap<String, String>();
        if (StringUtils.isNotBlank(prefix)) {
            queryParams.put("prefix", prefix);
        }
        if (StringUtils.isNotBlank(startKey)) {
            queryParams.put("startKey", startKey);
        }
        if (StringUtils.isNotBlank(nextToken)) {
            queryParams.put("nextToken", nextToken);
        }
        if (StringUtils.isNotBlank(limit)) {
            queryParams.put("limit", limit);
        }
        if (isPublic != null) {
            queryParams.put("isPublic", String.valueOf(isPublic));
        }
        return queryParams;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getStartKey() {
        return startKey;
    }

    public void setStartKey(String startKey) {
        this.startKey = startKey;
    }

    public String getNextToken() {
        return nextToken;
    }

    public void setNextToken(String nextToken) {
        this.nextToken = nextToken;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }
}
