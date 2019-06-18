package com.aliyuncs.fc.response;

import com.aliyuncs.fc.http.HttpResponse;
import com.aliyuncs.fc.model.ReservedCapacityMetaData;

public class ListReservedCapacitiesResponse extends HttpResponse {
    private ReservedCapacityMetaData[] reservedCapacities = null;
    private String nextToken = null;

    public ReservedCapacityMetaData[] getReservedCapacities() {
        return reservedCapacities;
    }

    public ListReservedCapacitiesResponse setReservedCapacities(ReservedCapacityMetaData[] reservedCapacities) {
        this.reservedCapacities = reservedCapacities;
        return this;
    }

    public String getNextToken() {
        return nextToken;
    }

    public ListReservedCapacitiesResponse setNextToken(String nextToken) {
        this.nextToken = nextToken;
        return this;
    }
}
