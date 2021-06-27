package com.java.note.Jdk.reference;

import java.lang.ref.WeakReference;

/**
 * @Author : mmy
 * @Creat Time : 2020/7/27  10:56
 * @Description 只要是 一律回收
 */
public class WeakReferenceDemo {


    public static void main(String[] args) {
        Object o = new Object();
        WeakReference weakReference = new WeakReference<>(o);
        System.out.println(o);
        System.out.println(weakReference.get());
        o = null;
        System.gc();
        System.out.println("======================");
        System.out.println(o);
        //null 会被回收掉
        System.out.println(weakReference.get());
    }
}
