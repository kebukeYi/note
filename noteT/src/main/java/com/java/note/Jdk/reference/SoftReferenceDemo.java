package com.java.note.Jdk.reference;

import java.lang.ref.SoftReference;

/**
 * @Author : mmy
 * @Creat Time : 2020/7/27  10:56
 * @Description 软引用有内存 就不回收 没有内存就 回收
 */
public class SoftReferenceDemo {

    public static void softMemoryEnough() {
        Object o = new Object();
        SoftReference<Object> softReference = new SoftReference<>(o);
        System.out.println(o);
        System.out.println(softReference.get());
        o = null;
        System.gc();
        System.out.println("======================");
        System.out.println(o);
        System.out.println(softReference.get());
    }

    /**
     * -Xms5m -Xmx5m -XX:+PrintGCDetails
     */
    public static void softMemoryNotEnough() {
        Object o = new Object();
        SoftReference<Object> softReference = new SoftReference<>(o);
        System.out.println(o);
        System.out.println(softReference.get());
        o = null;
        try {
            byte[] bytes = new byte[30 * 1024 * 1024];
            System.out.println("======================");
        } finally {
            System.out.println(o);
            System.out.println(softReference.get());
        }


    }


    public static void main(String[] args) {
        softMemoryEnough();
//        softMemoryNotEnough();
    }
}
