package com.java.note.Jdk.thread.LeeCode;

/**
 * @Author : mmy
 * @Creat Time : 2020/4/20  10:32
 * @Description
 */
public class H2O {

    Object lock = new Object();
    volatile int flag = 1;

    public H2O() {

    }

    public void hydrogen(Runnable releaseHydrogen) throws InterruptedException {
        synchronized (lock) {
            // releaseHydrogen.run() outputs "H". Do not change or remove this line.
            while (flag != 1 || flag != 2) {
                lock.wait();
            }
            if (flag == 1 || flag == 2) {
                flag++;
                releaseHydrogen.run();
                lock.notifyAll();
            }
        }
    }

    public void oxygen(Runnable releaseOxygen) throws InterruptedException {
        synchronized (lock) {
            while (flag != 3) {
                lock.wait();
            }
            if (flag == 3) {
                // releaseOxygen.run() outputs "O". Do not change or remove this line.
                releaseOxygen.run();
                flag = 1;
                lock.notifyAll();
            }
        }
    }
}
