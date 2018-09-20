package com.aliyuncs.fc.request;

import com.aliyuncs.fc.constants.Const;
import com.aliyuncs.fc.exceptions.ClientException;
import com.aliyuncs.fc.http.HttpRequest;
import com.aliyuncs.fc.model.ListRequestUrlHelper;
import com.aliyuncs.fc.response.ListCustomDomainsResponse;

import java.util.Map;

public class ListCustomDomainsRequest extends HttpRequest {
    private String prefix;
    private String startKey;
    private String nextToken;
    private Integer limit;

    public ListCustomDomainsRequest setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public String getPrefix() {
        return prefix;
    }

    public ListCustomDomainsRequest setStartKey(String startKey) {
        this.startKey = startKey;
        return this;
    }

    public String getStartKey() {
        return startKey;
    }

    public ListCustomDomainsRequest setNextToken(String nextToken) {
        this.nextToken = nextToken;
        return this;
    }

    public String getNextToken() {
        return nextToken;
    }

    public ListCustomDomainsRequest setLimit(int limit) {
        this.limit = limit;
        return this;
    }

    public Integer getLimit() {
        return limit;
    }

    public String getPath() {
        return String.format(Const.CUSTOM_DOMAIN_PATH, Const.API_VERSION);
    }

    public Map<String, String> getQueryParams() {
        return ListRequestUrlHelper.buildParams(prefix, startKey, nextToken, limit);
    }

    public byte[] getPayload() {
        return null;
    }

    public void validate() throws ClientException {
        return;
    }

    public Class<ListCustomDomainsResponse> getResponseClass() {
        return ListCustomDomainsResponse.class;
    }

}
