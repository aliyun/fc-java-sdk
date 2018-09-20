package com.aliyuncs.fc.request;

import com.aliyuncs.fc.constants.Const;
import com.aliyuncs.fc.exceptions.ClientException;
import com.aliyuncs.fc.http.HttpRequest;
import com.aliyuncs.fc.model.PathConfig;
import com.aliyuncs.fc.model.RouteConfig;
import com.aliyuncs.fc.response.UpdateCustomDomainResponse;
import com.aliyuncs.fc.response.UpdateServiceResponse;
import com.aliyuncs.fc.utils.ParameterHelper;
import com.google.common.base.Strings;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class UpdateCustomDomainRequest extends HttpRequest {

    private transient final String domainName;

    @SerializedName("protocol")
    private String protocol;

    @SerializedName("routeConfig")
    private RouteConfig routeConfig;

    public UpdateCustomDomainRequest(String domainName, String protocol, RouteConfig routeConfig) {
        this.domainName = domainName;
        this.protocol = protocol;
        this.routeConfig = routeConfig;
    }

    public UpdateCustomDomainRequest(String domainName) {
        this.domainName = domainName;
    }

    public String getDomainName() {
        return domainName;
    }

    public String getProtocol() {
        return protocol;
    }

    public UpdateCustomDomainRequest setProtocol(String protocol) {
        this.protocol = protocol;
        return this;
    }

    public RouteConfig getRouteConfig() {
        return routeConfig;
    }

    public UpdateCustomDomainRequest setRouteConfig(RouteConfig routeConfig) {
        this.routeConfig = routeConfig;
        return this;
    }

    public Map<String, String> getQueryParams() {
        return null;
    }

    public String getPath() {
        return String.format(Const.SINGLE_CUSTOM_DOMAIN_PATH, Const.API_VERSION, this.domainName);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public byte[] getPayload() {
        return ParameterHelper.ObjectToJson(this).getBytes();
    }

    public void validate() throws ClientException {
        if (Strings.isNullOrEmpty(domainName)) {
            throw new ClientException("Domain name cannot be blank");
        }
    }

    public Class<UpdateCustomDomainResponse> getResponseClass() {
        return UpdateCustomDomainResponse.class;
    }

}
