package com.aliyuncs.fc.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

public class Util {
    public static byte[] createZipByteData(String filename, String code) throws IOException {

        // Setup code directory
        String tmpDir = "/tmp/fc_test_" + UUID.randomUUID();
        String funcFilePath = tmpDir + "/" + filename;
        new File(tmpDir).mkdir();
        PrintWriter out = new PrintWriter(funcFilePath);
        out.println(code);
        out.close();
        String zipFilePath = tmpDir + "/" + "main.zip";
        ZipUtils.zipDir(new File(tmpDir), zipFilePath);

        File zipFile = new File(zipFilePath);
        byte[] buffer = new byte[(int) zipFile.length()];
        FileInputStream fis = new FileInputStream(zipFilePath);
        fis.read(buffer);
        fis.close();

        new File(funcFilePath).delete();
        new File(zipFilePath).delete();
        new File(tmpDir).delete();

        return buffer;
    }
}
