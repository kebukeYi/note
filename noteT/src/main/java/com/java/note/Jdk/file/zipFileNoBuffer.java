package com.java.note.Jdk.file;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @Author : fang.com
 * @CreatTime : 2020-11-23 14:29
 * @Description :
 * @Version :  0.0.1
 */
public class zipFileNoBuffer {

    private static final Strings ZIP_FILE = "";
    private static final Strings JPG_FILE = "";
    private static final Strings FILE_NAME = "";

    public static void zipFileNoBuffers() {
        File zipFile = new File(ZIP_FILE);
        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile))) {
            //开始时间
            long beginTime = System.currentTimeMillis();

            for (int i = 0; i < 10; i++) {
                try (InputStream input = new FileInputStream(JPG_FILE)) {
                    zipOut.putNextEntry(new ZipEntry(FILE_NAME + i));
                    int temp = 0;
                    while ((temp = input.read()) != -1) {
                        zipOut.write(temp);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
