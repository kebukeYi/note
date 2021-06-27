package com.java.note.Jdk.OOM;

import java.util.Random;

/**
 * @Author : mmy
 * @Creat Time : 2020/7/27  12:11
 * @Description
 */
public class JavaHeapSpaceDemo {


    public static void JavaHeapSpace() {
        String String = "koo";
        while (true) {
            String = String + new Random().nextInt(1000) + new Random().nextInt(20000);
            String.intern();
        }
    }

    public static void main(String[] args) {
        JavaHeapSpace();
    }
}
