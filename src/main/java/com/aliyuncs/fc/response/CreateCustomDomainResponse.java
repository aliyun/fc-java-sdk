package com.aliyuncs.fc.response;

import com.aliyuncs.fc.http.HttpResponse;
import com.aliyuncs.fc.model.CustomDomainMetaData;
import com.aliyuncs.fc.model.CustomDomainMetaData;
import com.aliyuncs.fc.model.PathConfig;
import com.aliyuncs.fc.model.RouteConfig;
import com.google.common.base.Preconditions;

public class CreateCustomDomainResponse extends HttpResponse {
    private CustomDomainMetaData customDomainMetaData;

    public CreateCustomDomainResponse setCustomDomainMetadata(CustomDomainMetaData customDomainMetaData) {
        this.customDomainMetaData = customDomainMetaData;
        return this;
    }

    public String getDomainName() {
        Preconditions.checkArgument(customDomainMetaData != null);
        return customDomainMetaData.getDomainName();
    }

    public String getAccountId() {
        Preconditions.checkArgument(customDomainMetaData != null);
        return customDomainMetaData.getAccountId();
    }

    public String getProtocol() {
        Preconditions.checkArgument(customDomainMetaData != null);
        return customDomainMetaData.getProtocol();
    }

    public String getAPIVersion() {
        Preconditions.checkArgument(customDomainMetaData != null);
        return customDomainMetaData.getApiVersion();
    }

    public RouteConfig getRouteConfig() {
        Preconditions.checkArgument(customDomainMetaData != null);
        return customDomainMetaData.getRouteConfig();
    }

    public String getCreatedTime() {
        Preconditions.checkArgument(customDomainMetaData != null);
        return customDomainMetaData.getCreatedTime();
    }

    public String getLastModifiedTime() {
        Preconditions.checkArgument(customDomainMetaData != null);
        return customDomainMetaData.getLastModifiedTime();
    }
}
