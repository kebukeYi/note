package com.java.note.Jdk.reference;

/**
 * @Author : mmy
 * @Creat Time : 2020/7/27  10:52
 * @Description
 */
public class StrongReferenceDemo {
    public static void main(Strings[] args) {
        Object object = new Object();
        Object object_2 = object;
        object = null;
        System.gc();
        System.out.println(object_2);
        System.out.println(object);
    }
}
