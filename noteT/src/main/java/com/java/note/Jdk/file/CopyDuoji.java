package com.java.note.Jdk.file;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author : kebukeYi
 * @date :  2021-08-02 21:05
 * @description: 失败
 * @question:
 * @link:
 **/
public class CopyDuoji {

    public static void main(String[] args) throws IOException {

        String srcFolder = "F:\\BaiduNetdiskDownload\\极客时间拷贝";
        String destFolder = "F:\\BaiduNetdiskDownload\\极客";

        File srcFolder11 = new File(srcFolder);
        File destFolder11 = new File(destFolder);

        copyFile(srcFolder11, destFolder11);
        System.out.println("************************");
        System.out.println("ok");

    }

    //destFolder F:\BaiduNetdiskDownload\极客
    private static void copyFile(File srcFolder, File destFolder) throws IOException {
        if (srcFolder.isDirectory()) {
            File[] fileArrays = srcFolder.listFiles();
            for (File file : fileArrays) {
                if (file.isDirectory()) {//F:\BaiduNetdiskDownload\极客时间拷贝\01-数据结构与算法之美
                    String absolutePath = destFolder.getAbsolutePath();
                    final String fileName = file.getName();
                    absolutePath = absolutePath + "\\" + fileName + "\\";
                    File newMkdir = new File(absolutePath);//F:\BaiduNetdiskDownload\极客\01-数据结构与算法之美
                    newMkdir.mkdir();
                    destFolder = (newMkdir);
                    copyFile(file, destFolder);
                } else {
                    if (file.getName().endsWith(".html")) {
                        String absolutePath = destFolder.getAbsolutePath();
//                        String srcFolderName = srcFolder.getName();
//                        absolutePath = absolutePath + "\\" + srcFolderName + "\\";
//                        File newMkdir = new File(absolutePath);
//                        newMkdir.mkdir();
                        File newFile = new File(absolutePath, file.getName());
                        newFile.createNewFile();
                        copy(file, newFile);
                    }
                }
            }
        }
    }

    private static void copy(File file, File newFile) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(newFile));

        byte[] bys = new byte[1024];
        int len = 0;
        while ((len = bis.read(bys)) != -1) {
            bos.write(bys, 0, len);
        }
        bos.close();
        bis.close();
    }
}
