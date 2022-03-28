package com.java.note.Jdk.thread.synchronizedd;

/**
 * @ClassName Somthing
 * @Author kebukeyi
 * @Date 2022/3/28 17:38
 * @Description 猜一猜有什么用途这个方法？
 * @Version 1.0.0
 */
public class Something {


    public static void method(long x) throws InterruptedException {
        if (x > 0) {
            Object o = new Object();
            synchronized (o) {
                //自己超时等待 自己唤醒 释放锁了
                //等同于 Thread.sleep(0);
                o.wait(x);
            }
        }
    }
}
