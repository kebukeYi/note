package com.java.note.Jdk.OOM;

/**
 * @Author : mmy
 * @Creat Time : 2020/7/27  12:09
 * @Description
 */
public class StackOverflowErrorDemo {
    public static void StackOverflowError() {
        StackOverflowError();
    }

    public static void main(String[] args) {
        StackOverflowError();
    }

}
