package com.java.note.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

import java.nio.file.Paths;

/**
 * @Author : mmy
 * @Creat Time : 2020/9/24  上午 11:04
 * @Description https://www.cnblogs.com/huangting/p/11469259.html
 */
public class IndexUse {

    /**
     * 目的：从索引文件中拿数据
     * 1、获取输入流（通过dirReader）
     * 2、获取索引搜索对象（通过输入流来拿）
     * 3、获取查询对象（通过查询解析器来获取，解析器是通过分词器获取）
     * 4、获取包含关键字排前面的文档对象集合
     * 5、可以获取对应文档的内容
     */

    /**
     * 通过关键字在索引目录中查询
     *
     * @param indexDir 索引文件所在目录
     * @param q        关键字
     */
    public static void search(String indexDir, String q) throws Exception {
        FSDirectory indexDirectory = FSDirectory.open(Paths.get(indexDir));
//        注意:索引输入流不是new出来的，是通过目录读取工具类打开的
        IndexReader indexReader = DirectoryReader.open(indexDirectory);
//        获取索引搜索对象
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        Analyzer analyzer = new StandardAnalyzer();
        QueryParser queryParser = new QueryParser("contents", analyzer);
//        获取符合关键字的查询对象
        Query query = queryParser.parse(q);

        long start = System.currentTimeMillis();
//        获取关键字出现的前十次
        TopDocs topDocs = indexSearcher.search(query, 10);
        long end = System.currentTimeMillis();
        System.out.println("匹配 " + q + " ，总共花费" + (end - start) + "毫秒" + "查询到" + topDocs.totalHits + "个记录");

        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            int docID = scoreDoc.doc;
//            索引搜索对象通过文档下标获取文档
            Document doc = indexSearcher.doc(docID);
            System.out.println("通过索引文件：" + doc.get("fullPath") + "拿数据");
        }

        indexReader.close();
    }


    //查询索引测试
    public static void main(String[] args) {
        String indexDir = "D:\\lucenetemp\\lucene\\demo1";
        String q = "EarlyTerminating-Collector";
        try {
            IndexUse.search(indexDir, q);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
