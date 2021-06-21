package com.java.note.Jdk.reference;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

/**
 * @Author : mmy
 * @Creat Time : 2020/7/27  11:40
 * @Description
 */
public class ReferenceQueueDemo {


    public static void main(Strings[] args) throws InterruptedException {
        Object o = new Object();
        ReferenceQueue<Object> referenceQueue = new ReferenceQueue<>();
        WeakReference<Object> weakReference = new WeakReference<>(o, referenceQueue);
        //java.lang.Object@21b8d17c
        System.out.println(o);
        //java.lang.ref.WeakReference@6433a2
        System.out.println(weakReference.get());
        //null
        System.out.println(referenceQueue.poll());

        System.out.println("========================");
        o = null;
        System.gc();
        Thread.sleep(500);

        //null
        System.out.println(o);
        //null
        System.out.println(weakReference.get());

        //加入到了引用队列中
        //java.lang.ref.WeakReference@6433a2
        System.out.println(referenceQueue.poll());
    }
}
