package com.aliyuncs.fc.utils;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * U
 */
public class ZipUtils {

    public static void zipDir(File dir, String zipName) throws IOException {
        Preconditions.checkArgument(dir != null, "dir `"+ dir + "` cannot be null");
        Preconditions.checkArgument(dir.isDirectory(), "dir `"+ dir + "` must be a directory");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(zipName), "zipName cannot be blank");
        List<String> fileNames = new ArrayList<String>();
        getFileNames(dir, fileNames);
        // zip files one by one
        // create ZipOutputStream to write to the zip file
        FileOutputStream fos = new FileOutputStream(zipName);
        ZipOutputStream zos = new ZipOutputStream(fos);
        for (String filePath : fileNames) {
            // for ZipEntry we need to keep only relative file path, so we used substring on absolute path
            ZipEntry ze = new ZipEntry(
                filePath.substring(dir.getAbsolutePath().length() + 1, filePath.length()));
            zos.putNextEntry(ze);
            // read the file and write to ZipOutputStream
            FileInputStream fis = new FileInputStream(filePath);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, len);
            }
            zos.closeEntry();
            fis.close();
        }
        zos.close();
        fos.close();
    }

    private static void getFileNames(File dir, List<String> fileNames) throws IOException {
        File[] files = dir.listFiles();

        for (File file : files) {
            if (file.isFile()) {
                fileNames.add(file.getAbsolutePath());
            } else {
                getFileNames(file, fileNames);
            }
        }
    }
}
