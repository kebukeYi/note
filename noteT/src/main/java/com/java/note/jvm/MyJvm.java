package com.java.note.jvm;

import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/25  10:38
 * @Description
 */
public class MyJvm {


    private native void start0();

    public static void main(String[] args) {
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName());
        }, "Thread-i").start();
        System.out.println(Thread.currentThread().getName());

        //返回虚拟机试图使用的最大内存
        long maxMemory = Runtime.getRuntime().maxMemory();
        long totalMemory = Runtime.getRuntime().totalMemory();


        System.out.println(maxMemory / 1024 / 1204 + " MB");
        System.out.println(totalMemory / 1024 / 1204 + " MB");
        newMemory();
        newLocalMemory();
    }

    public static void newMemory() {
        byte[] array = new byte[1 * 1024 * 1024];//1BM
        ArrayList<byte[]> arrayList = new ArrayList<>();
        int count = 1;
        try {
            while (true) {
                arrayList.add(array);
                count++;
            }
        } catch (Error e) {
            System.out.println("count : " + count);
            e.printStackTrace();
        }

    }


    public static void newLocalMemory() {
        System.out.println("maxDirectMemory is:" + sun.misc.VM.maxDirectMemory() / 1024 / 1024 + "MB");
        //ByteBuffer.allocate(capability) 是分配 JVM 堆内存，属于 GC 管辖范围，需要内存拷贝所以速度相对较慢
        //ByteBuffer buffer = ByteBuffer.allocate(6*1024*1024);

        //ByteBuffer.allocateDirect(capability) 是分配 OS 本地内存，不属于 GC 管辖范围，由于不需要内存拷贝所以速度相对较快；
        ByteBuffer buffer = ByteBuffer.allocateDirect(6 * 1024 * 1024);
    }
}
