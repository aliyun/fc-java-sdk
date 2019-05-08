package com.aliyuncs.fc.model;

import com.google.gson.annotations.SerializedName;

public class CertConfig {

    @SerializedName("certName")
    private String certName;

    @SerializedName("privateKey")
    private String privateKey;

    @SerializedName("certificate")
    private String certificate;

    public CertConfig() {

    }

    public CertConfig(String certName, String certificate, String privateKey) {
        this.certName = certName;
        this.certificate = certificate;
        this.privateKey = privateKey;
    }

    public String getCertName() {
        return certName;
    }

    public void setCertName(String certName) {
        this.certName = certName;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }
}
