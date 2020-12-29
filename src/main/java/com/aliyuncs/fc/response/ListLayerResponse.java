package com.aliyuncs.fc.response;

import com.aliyuncs.fc.http.HttpResponse;

public class ListLayerResponse extends HttpResponse {

    private Layer[] layers;

    private String nextToken;

    class Layer {
        private String createDate;
        private String description;
        private String version;
        private String[] compatibleRuntime;
        private String creatorUID;

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String[] getCompatibleRuntime() {
            return compatibleRuntime;
        }

        public void setCompatibleRuntime(String[] compatibleRuntime) {
            this.compatibleRuntime = compatibleRuntime;
        }

        public String getCreatorUID() {
            return creatorUID;
        }

        public void setCreatorUID(String creatorUID) {
            this.creatorUID = creatorUID;
        }
    }

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
}
