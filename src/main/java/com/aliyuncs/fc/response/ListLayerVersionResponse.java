package com.aliyuncs.fc.response;

import com.aliyuncs.fc.http.HttpResponse;

public class ListLayerVersionResponse extends HttpResponse {

    private LayerVersion[] layerVersions;

    private Integer nextVersion;

    public LayerVersion[] getLayerVersions() {
        return layerVersions;
    }

    public void setLayerVersions(LayerVersion[] layerVersions) {
        this.layerVersions = layerVersions;
    }

    public Integer getNextVersion() {
        return nextVersion;
    }

    public void setNextVersion(Integer nextVersion) {
        this.nextVersion = nextVersion;
    }

    class LayerVersion {
        private String layerName;
        private String createDate;
        private String description;
        private String version;
        private String[] compatibleRuntime;

        public String getLayerName() {
            return layerName;
        }

        public void setLayerName(String layerName) {
            this.layerName = layerName;
        }

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
    }

}
