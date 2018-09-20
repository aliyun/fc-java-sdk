package com.aliyuncs.fc.request;

import com.aliyuncs.fc.constants.Const;
import com.aliyuncs.fc.exceptions.ClientException;
import com.aliyuncs.fc.http.HttpRequest;
import com.aliyuncs.fc.response.GetCustomDomainResponse;
import com.google.common.base.Strings;

import java.util.Map;

public class GetCustomDomainRequest extends HttpRequest {
    private String domainName;

    public GetCustomDomainRequest(String domainName) {
        this.domainName = domainName;
    }

    public String getDomainName() {
        return domainName;
    }

    public String getPath() {
        return String.format(Const.SINGLE_CUSTOM_DOMAIN_PATH, Const.API_VERSION, domainName);
    }

    public Map<String, String> getQueryParams() {
        return null;
    }

    public byte[] getPayload() {
        return null;
    }

    public void validate() throws ClientException {
        if (Strings.isNullOrEmpty(domainName)) {
            throw new ClientException("Domain name cannot be blank");
        }
    }

    public Class<GetCustomDomainResponse> getResponseClass() {
        return GetCustomDomainResponse.class;
    }
}
