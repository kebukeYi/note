package com.java.note.jvm;

import org.openjdk.jol.info.ClassLayout;

/**
 * @Author : fang.com
 * @CreatTime : 2020-12-30 09:07
 * @Description : Object obj=new Object()占用字节
 * @Version :  0.0.1
 */
class HeapMemory {


    //-XX:+TraceClassLoading 可以看加载了哪些类
    public static void main(String[] args) {
        Object obj = new Object();
        MyItem myItem = new MyItem();
        System.out.println(ClassLayout.parseInstance(obj).toPrintable());
        System.out.println(ClassLayout.parseInstance(myItem).toPrintable());
    }
}
