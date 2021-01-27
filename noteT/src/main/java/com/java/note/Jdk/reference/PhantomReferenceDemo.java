package com.java.note.Jdk.reference;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

/**
 * @Author : mmy
 * @Creat Time : 2020/7/27  11:35
 * @Description 虚引用
 * 虚引用的主要作用是跟踪对象被垃圾回收的状态。仅仅是提供了一种确保对象被fnηaize以后,做某些事情的机制。
 * PhantomReference的get方法总是返回nu,因此无法访问对应的引用对象。其意义在于说明一个对象已经进入 finalization阶段,可以被
 * gc回收,用来实现比 finalization机制更灵活的回收操作。
 * <p>
 * GC 后 被放到引用队列中
 */
public class PhantomReferenceDemo {

    public static void main(String[] args) throws InterruptedException {
        Object o = new Object();
        PhantomReferenceDemo phantomReferenceDemo = new PhantomReferenceDemo();
        ReferenceQueue<PhantomReferenceDemo> referenceQueue = new ReferenceQueue<>();
        PhantomReference<PhantomReferenceDemo> phantomReference = new PhantomReference<>(phantomReferenceDemo, referenceQueue);
        //java.lang.Object@21b8d17c
        System.out.println(phantomReferenceDemo);
        //null
        System.out.println(phantomReference.get());
        //null
        System.out.println(referenceQueue.poll());
        System.out.println("========================");
        o = null;
        System.gc();
        Thread.sleep(500);
        //null
        System.out.println(phantomReferenceDemo);
        //null
        System.out.println(phantomReference.get());

        //加入到了引用队列中
        //java.lang.ref.PhantomReference@6433a2
        System.out.println(referenceQueue.poll());
    }

    @Override
    protected void finalize() throws Throwable {
//        super.finalize();
        System.out.println(this);
        System.out.println("要被回收");
    }
}
