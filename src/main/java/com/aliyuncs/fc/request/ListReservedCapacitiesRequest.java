package com.aliyuncs.fc.request;

import com.aliyuncs.fc.constants.Const;
import com.aliyuncs.fc.exceptions.ClientException;
import com.aliyuncs.fc.http.HttpRequest;
import com.aliyuncs.fc.model.ListRequestUrlHelper;
import com.aliyuncs.fc.response.ListReservedCapacitiesResponse;

import java.util.Map;

public class ListReservedCapacitiesRequest extends HttpRequest {
    private String nextToken;
    private Integer limit;

    public ListReservedCapacitiesRequest setNextToken(String nextToken) {
        this.nextToken = nextToken;
        return this;
    } 

    public String getNextToken() {
        return nextToken;
    }

    public ListReservedCapacitiesRequest setLimit(int limit) {
        this.limit = limit;
        return this;
    }

    public Integer getLimit() {
        return limit;
    }

    public String getPath() {
        return String.format(Const.RESERVED_CAPACITY_PATH, Const.API_VERSION);
    }

    public Map<String, String> getQueryParams() {
        return ListRequestUrlHelper.buildListReservedCapacitiesParams(nextToken, limit);
    }

    public byte[] getPayload() {
        return null;
    }

    public void validate() throws ClientException {
        return;
    }

    public Class<ListReservedCapacitiesResponse> getResponseClass() {
        return ListReservedCapacitiesResponse.class;
    }

}
