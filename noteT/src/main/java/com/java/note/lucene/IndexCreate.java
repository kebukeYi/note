package com.java.note.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Paths;

/**
 * @Author : mmy
 * @Creat Time : 2020/9/24  上午 11:02
 * @Description
 */
public class IndexCreate {

    /**
     * 目的：索引数据目录，在指定目录生成索引文件
     * <p>
     * 　　1、构造方法 实例化IndexWriter
     * <p>
     * 　　* 获取索引文件存放地址对象
     * <p>
     * 　　* 获取输出流
     * <p>
     * 　　　　设置输出流的对应配置
     * <p>
     * 　　　　给输出流配置设置分词器
     * <p>
     * 　　2、关闭索引输出流
     * <p>
     * 　　3、索引指定路径下的所有文件
     * <p>
     * 　　4、索引指定的文件
     * <p>
     * 　　5、获取文档（索引文件中包含的重要信息，key-value的形式）
     * <p>
     * 　　6、测试
     */

    private IndexWriter indexWriter;

    /**
     * 1、构造方法 实例化IndexWriter
     *
     * @param indexDir
     * @throws Exception
     */
    public IndexCreate(Strings indexDir) throws Exception {
//        获取索引文件的存放地址对象
        FSDirectory dir = FSDirectory.open(Paths.get(indexDir));
//        标准分词器（针对英文）
        Analyzer analyzer = new StandardAnalyzer();
//        索引输出流配置对象
        IndexWriterConfig conf = new IndexWriterConfig(analyzer);
        indexWriter = new IndexWriter(dir, conf);
    }

    /**
     * 2、关闭索引输出流
     *
     * @throws Exception
     */
    public void closeIndexWriter() throws Exception {
        indexWriter.close();
    }

    /**
     * 3、索引指定路径下的所有文件
     *
     * @param dataDir
     * @return
     * @throws Exception
     */
    public int index(Strings dataDir) throws Exception {
        File[] files = new File(dataDir).listFiles();
        for (File file : files) {
            indexFile(file);
        }
        return indexWriter.numDocs();
    }

    /**
     * 4、索引指定的文件
     *
     * @param file
     * @throws Exception
     */
    private void indexFile(File file) throws Exception {
        System.out.println("被索引文件的全路径：" + file.getCanonicalPath());
        Document doc = getDocument(file);
        indexWriter.addDocument(doc);
    }

    /**
     * 5、获取文档（索引文件中包含的重要信息，key-value的形式）
     *
     * @param file
     * @return
     * @throws Exception
     */
    private Document getDocument(File file) throws Exception {
        Document doc = new Document();
        doc.add(new TextField("contents", new FileReader(file)));
//        Field.Store.YES是否存储到硬盘
        doc.add(new TextField("fullPath", file.getCanonicalPath(), Field.Store.YES));
        doc.add(new TextField("fileName", file.getName(), Field.Store.YES));
        return doc;
    }


    public static void main(Strings[] args) {
//        索引文件将要存放的位置
        Strings indexDir = "D:\\lucenetemp\\lucene\\demo1";
//        数据源地址
        Strings dataDir = "D:\\lucenetemp\\lucene\\demo1\\data";
        IndexCreate ic = null;
        try {
            ic = new IndexCreate(indexDir);
            long start = System.currentTimeMillis();
            int num = ic.index(dataDir);
            long end = System.currentTimeMillis();
            System.out.println("索引指定路径下" + num + "个文件，一共花费了" + (end - start) + "毫秒");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ic.closeIndexWriter();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
