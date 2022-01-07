package com.mmy.nio.doc;

import com.spire.doc.Document;
import com.spire.doc.DocumentObject;
import com.spire.doc.FileFormat;
import com.spire.doc.Section;
import com.spire.doc.documents.Paragraph;
import com.spire.doc.fields.TextRange;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Objects;

/**
 * @author : kebukeYi
 * @date :  2022-01-07 21:58
 * @description: Java 操作 doc 文件
 * @question:
 * @link: https://www.e-iceblue.cn/spiredocforjavaoperating/add-content-controls-to-word-in-java.html
 **/
public class MergeWordDocument {

    private static ArrayList<String> arrayList = new ArrayList<>();

    public static void findFile(File file, FileChannel destFileName, String suffix) {
        if (file.isDirectory()) {
            //F:\\BaiduNetdiskDownload\\DFS\\001~021
            for (File subFile : Objects.requireNonNull(file.listFiles())) {
                if (subFile.isDirectory()) {
                    findFile(subFile, destFileName, suffix);
                } else {
                    final String subFileName = subFile.getName();
                    if (subFileName.endsWith(suffix)) {
                        final String name = file.getName();
                        //System.out.println(name);
                        try {
                            final String filePath = subFile.getPath();
                            //System.out.println(filePath);
                            arrayList.add(filePath);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public static void mergeFiles(String outFile, ArrayList<String> files) {
        //加载第一个文档
        Document document = new Document(outFile);

        for (String file : files) {
            final String[] split = file.split("\\\\");
            final String name = "标题：" + split[4];
            Document document2 = new Document(file);
            Section section = document.addSection();
            Paragraph paragraph = section.addParagraph();
            paragraph.appendText(name);
            section.addParagraph();

            //使用insertTextFromFile方法将第二个文档的内容插入到第一个文档
            //document.insertTextFromFile(file, FileFormat.Docx_2013);

            //获取第一个文档的最后一个section
            Section lastSection = document.getLastSection();

            //将第二个文档的段落作为新的段落添加到第一个文档的最后一个section
            for (Section sec : (Iterable<Section>) document2.getSections()) {
                for (DocumentObject obj : (Iterable<DocumentObject>) sec.getBody().getChildObjects()) {
                    lastSection.getBody().getChildObjects().add(obj.deepClone());
                }
            }
        }
        //保存文档
        document.saveToFile("Output.doc", FileFormat.Docx_2013);
    }


    public static void main(String[] args) {
        String path = "F:\\BaiduNetdiskDownload\\DFS";
        String suffix = ".doc";
        String destFileName = "F:\\BaiduNetdiskDownload\\sun.docx";
        findFile(new File(path), null, suffix);
        System.out.println(arrayList.size());
        mergeFiles(destFileName, arrayList);
    }
}
 
