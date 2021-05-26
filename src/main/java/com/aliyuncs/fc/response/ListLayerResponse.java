package com.aliyuncs.fc.response;

import com.aliyuncs.fc.http.HttpResponse;
import com.aliyuncs.fc.model.Layer;

import java.util.Arrays;

public class ListLayerResponse extends HttpResponse {

    private Layer[] layers;

    private String nextToken;

    public Layer[] getLayers() {
        return layers;
    }

    public void setLayers(Layer[] layers) {
        this.layers = layers;
    }

    public String getNextToken() {
        return nextToken;
    }

    public void setNextToken(String nextToken) {
        this.nextToken = nextToken;
    }

    @Override
    public String toString() {
        return "ListLayerResponse{" +
                "layers=" + Arrays.toString(layers) +
                ", nextToken='" + nextToken + '\'' +
                '}';
    }
}
