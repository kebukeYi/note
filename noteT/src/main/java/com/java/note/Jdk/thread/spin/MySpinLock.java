package com.java.note.Jdk.thread.spin;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @ClassName MySpinLock
 * @Author kebukeyi
 * @Date 2022/3/28 11:41
 * @Description 简版自旋锁
 * @Version 1.0.0
 */
public class MySpinLock {

    private final AtomicReference<Thread> reference = new AtomicReference<>();

    public void lock() {
        Thread currentThread = Thread.currentThread();
        System.out.println(Thread.currentThread() + " come in!");
        while (!reference.compareAndSet(null, currentThread)) {
            System.out.println("正在尝试获取锁 , ThreadName: " + Thread.currentThread());
        }
        System.out.println(Thread.currentThread() + " invoked myLock!");
    }


    public void unLock() {
        Thread currentThread = Thread.currentThread();
        reference.compareAndSet(currentThread, null);
        System.out.println(Thread.currentThread() + " invoked myUnLock!");
    }


    public static void main(String[] args) {
        MySpinLock spinLock = new MySpinLock();

        new Thread(() -> {
            spinLock.lock();
            try {
                //模拟持有锁后的业务流程
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                spinLock.unLock();
            }
        }, "T-1").start();

        new Thread(() -> {
            spinLock.lock();
            spinLock.unLock();
        }, "T-2").start();
    }
}
