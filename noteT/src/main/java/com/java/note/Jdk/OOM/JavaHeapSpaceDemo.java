package com.java.note.Jdk.OOM;

import java.util.Random;

/**
 * @Author : mmy
 * @Creat Time : 2020/7/27  12:11
 * @Description
 */
public class JavaHeapSpaceDemo {


    public static void JavaHeapSpace() {
        Strings strings = "koo";
        while (true) {
            strings = strings + new Random().nextInt(1000) + new Random().nextInt(20000);
            strings.intern();
        }
    }

    public static void main(Strings[] args) {
        JavaHeapSpace();
    }
}
