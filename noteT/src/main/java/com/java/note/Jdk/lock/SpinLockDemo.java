package com.java.note.Jdk.lock;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @Author : mmy
 * @Creat Time : 2020/7/20  17:54
 * @Description 自定义自旋锁
 */
public class SpinLockDemo {

    //初始化为null
    AtomicReference<Thread> atomicReference = new AtomicReference<>();

    public void lock() {
        Thread thread = Thread.currentThread();
        while (!atomicReference.compareAndSet(null, thread)) {
            System.out.println(Thread.currentThread().getName() + "\t" + "正在争抢资源");
        }
        System.out.println(Thread.currentThread().getName() + "\t" + "进来了");
    }

    public void unLock() {
        Thread thread = Thread.currentThread();
        atomicReference.compareAndSet(thread, null);
        System.out.println(Thread.currentThread().getName() + "\t" + "释放锁");

    }

    public static void main(String[] args) throws InterruptedException {
        SpinLockDemo spinLockDemo = new SpinLockDemo();
        new Thread(() -> {
            spinLockDemo.lock();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            spinLockDemo.unLock();
        }, "AA").start();

        new Thread(() -> {
            spinLockDemo.lock();
            spinLockDemo.unLock();
        }, "BB").start();


    }

}
