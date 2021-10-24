package com.java.interview.thread.wait_notify;

import com.java.interview.thread.consumer_provider.Print;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author : kebukeYi
 * @date :  2021-10-23 10:59
 * @description:
 * @question:
 * @link:
 **/
public class TestSync {

    private volatile Integer amount = 0;
    private final Object LOCK_OBJECT = new Object();
    private final Object FULL = new Object();
    private final Object EMPTY = new Object();

    public void doubleSync() throws InterruptedException {
        System.out.println("Producer trying to get LOCK_OBJECT lock ");
        synchronized (LOCK_OBJECT) {
            System.out.println("Producer get LOCK_OBJECT lock ");
            Print.sleep(3000);
            while (amount >= 0) {
                synchronized (FULL) {
                    // Thread.sleep(20000);
                    System.out.println("full！");
                    FULL.wait();
                }
            }
            System.out.println("continue~");
            amount++;
            System.out.println("Producer release LOCK_OBJECT lock ");
        }
    }

    public void simpleSync() {
        System.out.println("Consumer trying to get LOCK_OBJECT lock ");
        synchronized (LOCK_OBJECT) {
            System.out.println("Consumer get LOCK_OBJECT lock");
        }
        System.out.println("Consumer release LOCK_OBJECT lock");
    }

    public static void main(String[] args) throws InterruptedException {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        final TestSync testSync = new TestSync();
        ExecutorService threadPool = Executors.newFixedThreadPool(1, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                int index = atomicInteger.incrementAndGet();
                // System.out.println("create no " + index + " thread");
                Thread t = new Thread(r, "one Thread-" + index);
                return t;
            }
        });
        threadPool.execute(() -> {
            try {
                testSync.doubleSync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Print.sleep(10000);
        ExecutorService executorService = Executors.newFixedThreadPool(1, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                int index = atomicInteger.incrementAndGet();
                //System.out.println("create no " + index + " thread");
                Thread t = new Thread(r, "two Thread-" + index);
                return t;
            }
        });
        executorService.execute(() -> {
            try {
                testSync.simpleSync();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
 
