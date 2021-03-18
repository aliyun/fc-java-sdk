package com.aliyuncs.fc.response;

import com.aliyuncs.fc.http.HttpResponse;

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

    class Layer {
        private String layerName;
        private String version;
        private String description;
        private Object code;
        private Integer codeSize;
        private String codeChecksum;
        private String createTime;
        private String[] compatibleRuntime;
        private Integer acl;
        private String Arn;

        public String getLayerName() {
            return layerName;
        }

        public void setLayerName(String layerName) {
            this.layerName = layerName;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Object getCode() {
            return code;
        }

        public void setCode(Object code) {
            this.code = code;
        }

        public Integer getCodeSize() {
            return codeSize;
        }

        public void setCodeSize(Integer codeSize) {
            this.codeSize = codeSize;
        }

        public String getCodeChecksum() {
            return codeChecksum;
        }

        public void setCodeChecksum(String codeChecksum) {
            this.codeChecksum = codeChecksum;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String[] getCompatibleRuntime() {
            return compatibleRuntime;
        }

        public void setCompatibleRuntime(String[] compatibleRuntime) {
            this.compatibleRuntime = compatibleRuntime;
        }

        public Integer getAcl() {
            return acl;
        }

        public void setAcl(Integer acl) {
            this.acl = acl;
        }

        public String getArn() {
            return Arn;
        }

        public void setArn(String arn) {
            this.Arn = arn;
        }
    }

}
