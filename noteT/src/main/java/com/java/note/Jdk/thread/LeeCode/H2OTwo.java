package com.java.note.Jdk.thread.LeeCode;

import java.util.concurrent.Semaphore;

/**
 * @Author : mmy
 * @Creat Time : 2020/4/20  11:10
 * @Description
 */
public class H2OTwo {

    Semaphore h = new Semaphore(2);
    Semaphore o = new Semaphore(0);

    public H2OTwo() {

    }

    public void hydrogen(Runnable releaseHydrogen) throws InterruptedException {
        h.acquire();
        // releaseHydrogen.run() outputs "H". Do not change or remove this line.
        releaseHydrogen.run();
        o.release();
    }

    public void oxygen(Runnable releaseOxygen) throws InterruptedException {
        o.acquire(2);
        // releaseOxygen.run() outputs "O". Do not change or remove this line.
        releaseOxygen.run();
        h.release(2);
    }


}
