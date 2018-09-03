package com.aliyuncs.fc.request;

import com.aliyuncs.fc.constants.Const;
import com.aliyuncs.fc.exceptions.ClientException;
import com.aliyuncs.fc.http.HttpRequest;
import com.aliyuncs.fc.response.DeleteCustomDomainResponse;
import com.google.common.base.Strings;

import java.util.Map;

public class DeleteCustomDomainRequest extends HttpRequest {
    private String domainName;

    public DeleteCustomDomainRequest(String domainName) {
        this.domainName = domainName;
    }

    public String getDomainName() {
        return domainName;
    }

    public String getPath() {
        return String.format(Const.SINGLE_CUSTOM_DOMAIN_PATH, Const.API_VERSION, this.domainName);
    }

    public Map<String, String> getQueryParams() {
        return null;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public byte[] getPayload() {
        return null;
    }

    public void validate() throws ClientException {
        if (Strings.isNullOrEmpty(domainName)) {
            throw new ClientException("Domain name cannot be blank");
        }
    }

    public Class<DeleteCustomDomainResponse> getResponseClass() {
        return DeleteCustomDomainResponse.class;
    }

}
