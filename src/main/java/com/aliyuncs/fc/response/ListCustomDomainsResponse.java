package com.aliyuncs.fc.response;

import com.aliyuncs.fc.http.HttpResponse;
import com.aliyuncs.fc.model.CustomDomainMetaData;
import com.aliyuncs.fc.model.ServiceMetadata;

public class ListCustomDomainsResponse extends HttpResponse {
    private CustomDomainMetaData[] customDomains = null;
    private String nextToken = null;

    public CustomDomainMetaData[] getCustomDomains() {
        return customDomains;
    }

    public ListCustomDomainsResponse setCustomDomains(CustomDomainMetaData[] customDomains) {
        this.customDomains = customDomains;
        return this;
    }

    public String getNextToken() {
        return nextToken;
    }

    public ListCustomDomainsResponse setNextToken(String nextToken) {
        this.nextToken = nextToken;
        return this;
    }
}
