package com.aliyuncs.fc.model;

import com.aliyuncs.fc.utils.Base64Helper;
import com.aliyuncs.fc.utils.ZipUtils;
import com.google.gson.annotations.SerializedName;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * TODO: add javadoc
 */
public class Code {

    @SerializedName("ossBucketName")
    public String ossBucketName;

    @SerializedName("ossObjectName")
    public String ossObjectName;

    @SerializedName("zipFile")
    public String zipFile;

    public Code setOssBucketName(String ossBucketName) {
        this.ossBucketName = ossBucketName;
        return this;
    }

    public String getOssBucketName() {
        return ossBucketName;
    }

    public Code setOssObjectName(String ossObjectName) {
        this.ossObjectName = ossObjectName;
        return this;
    }

    public String getOssObjectName() {
        return ossObjectName;
    }

    public Code setZipFile(byte[] zipFile) {
        this.zipFile = Base64Helper.encode(zipFile);
        return this;
    }

    public String getZipFile() {
        return zipFile;
    }

    public Code setDir(String dir) throws IOException {
        String tempZipPath = "/tmp/code" + UUID.randomUUID() + ".zip";
        ZipUtils.zipDir(new File(dir), tempZipPath);
        File file = new File(tempZipPath);
        byte[] buffer = new byte[file.length() > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int)file.length()];
        FileInputStream fis = new FileInputStream(tempZipPath);
        fis.read(buffer);
        fis.close();
        this.zipFile = Base64Helper.encode(buffer);
        new File(tempZipPath).delete();
        return this;
    }
}
