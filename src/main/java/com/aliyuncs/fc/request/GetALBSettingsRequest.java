package com.aliyuncs.fc.request;

import com.aliyuncs.fc.constants.Const;
import com.aliyuncs.fc.exceptions.ClientException;
import com.aliyuncs.fc.http.HttpRequest;
import com.aliyuncs.fc.response.GetAccountSettingsResponse;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class GetALBSettingsRequest extends HttpRequest {

    @SerializedName("accountId")
    private String accountId;

    public GetALBSettingsRequest() {}

    public String getPath() {
        return String.format(Const.ALB_SETTING_PATH, Const.API_VERSION, accountId);
    }

    public void validate() throws ClientException {
    }

    @Override
    public Map<String, String> getQueryParams() {
        Map<String, String> queryParams = new HashMap<String, String>();
        if (StringUtils.isNotBlank(accountId)) {
            queryParams.put("accountId", accountId);
        }
        return queryParams;
    }

    public Class<GetAccountSettingsResponse> getResponseClass() {
        return GetAccountSettingsResponse.class;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
