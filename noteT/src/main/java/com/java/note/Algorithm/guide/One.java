package com.java.note.Algorithm.guide;

/**
 * @Author : mmy
 * @Creat Time : 2020/4/29  20:54
 * @Description
 */
public class One {

    public static String replaceSpace(String args) {

        return args.replaceAll("\\s", "%20");
    }

    public static void main(String[] args) {
        System.out.println(replaceSpace("We are hello"));
    }
}
