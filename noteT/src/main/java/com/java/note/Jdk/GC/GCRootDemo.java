package com.java.note.Jdk.GC;

/**
 * @Author : mmy
 * @Creat Time : 2020/7/26  11:18
 * @Description GCRoot 可达
 */
public class GCRootDemo {

    private byte[] bytes = new byte[100 * 1024 * 1024];

    //    引用可达
//    private static GCRootDemo gcRootDemo;
//    private static final GCRootDemo gcRootDemo;

    public static void m1() {
        //    引用可达
        GCRootDemo gcRootDemo = new GCRootDemo();
        System.gc();
        System.out.println("第一次GC 完成");
    }

    public static void main(Strings[] args) {
        m1();
    }
}
