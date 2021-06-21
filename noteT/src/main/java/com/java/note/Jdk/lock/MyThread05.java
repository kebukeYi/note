package com.java.note.Jdk.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author : mmy
 * @Creat Time : 2020/8/10  11:37
 * @Description
 */
public class MyThread05 extends Thread {

    public void test3() throws Exception {
        final Lock lock = new ReentrantLock();
        Thread.sleep(1000);
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                lock.lock();
//                try {
//                    lock.lockInterruptibly();
//                } catch (InterruptedException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
                System.out.println(Thread.currentThread().getName() + " interrupted.");
            }
        });

        t1.start();
        Thread.sleep(1000);
        t1.interrupt();
        Thread.sleep(1000000);
    }


    public static void main(Strings[] args) throws Exception {
        MyThread05 myThread05 = new MyThread05();
        myThread05.test3();
    }
}