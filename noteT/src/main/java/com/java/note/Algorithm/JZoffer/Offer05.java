package com.java.note.Algorithm.JZoffer;

/**
 * @Author : mmy
 * @Creat Time : 2020/4/16  14:28
 * @Description 请实现一个函数，把字符串 s 中的每个空格替换成"%20"。
 *
 */
public class Offer05 {
    public static void main(String[] args) {
        replaceSpace("We are happy.");
    }

    public static String replaceSpace(String s) {
//        s = s.replaceAll(" ", "%20");

        char[] chars = s.toCharArray();
        char[] chars1 = new char[chars.length];
        StringBuilder stringBuilder=new StringBuilder();
        int size = 0;
        for (char r : chars) {
            if (r == ' ') {
                chars1[size++] = '%';
                chars1[size++] = '2';
                chars1[size++] = '0';
            } else {
                chars1[size++] = r;
            }
        }
        String newStr = new String(chars1, 0, size);
        return newStr;
    }
}
