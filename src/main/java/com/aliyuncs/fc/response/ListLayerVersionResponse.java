package com.aliyuncs.fc.response;

import com.aliyuncs.fc.http.HttpResponse;
import com.aliyuncs.fc.model.Layer;

import java.util.Arrays;

public class ListLayerVersionResponse extends HttpResponse {

    private Layer[] layers;

    private Integer nextVersion;

    public Layer[] getLayers() {
        return layers;
    }

    public void setLayers(Layer[] layers) {
        this.layers = layers;
    }

    public Integer getNextVersion() {
        return nextVersion;
    }

    public void setNextVersion(Integer nextVersion) {
        this.nextVersion = nextVersion;
    }

    @Override
    public String toString() {
        return "ListLayerVersionResponse{" +
                "layers=" + Arrays.toString(layers) +
                ", nextVersion=" + nextVersion +
                '}';
    }
}
