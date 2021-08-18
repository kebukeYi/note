package com.java.note.Jdk.string;

/**
 * @author : kebukeYi
 * @date :  2021-08-03 00:57
 * @description:
 * @question:
 * @link:
 **/
public class StringDemo {

    public static void main(String[] args) {
        String str = "F:\\BaiduNetdiskDownload\\copy_a\\01-数据结构与算法之美\\01-开篇词 (1讲)";
        final int lastIndexOf = str.lastIndexOf("\\");
//        str = str.substring(lastIndexOf + 1, str.length());
//        System.out.println(str);
        final String substring = str.substring(0, lastIndexOf);
        System.out.println(substring);

    }

}
 
