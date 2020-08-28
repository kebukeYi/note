package com.java.note.Jdk.lock.dflockTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @Author : mmy
 * @Creat Time : 2020/8/10  16:38
 * @Description
 */
public class RTTTest {

    private static int count;

    private static int readThreadNum = 0;
    private static int writeThreadNum = 1;

    private static int maxValue = 1000;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public static void main(String[] args) {
        Counter lockTest = new RTTTest().new Counter();
        long startTime = System.currentTimeMillis();
        CountDownLatch latch = new CountDownLatch(readThreadNum + writeThreadNum);
        for (int i = 0; i < writeThreadNum; i++) {
            new Thread(() -> {
                for (int cur = 0; cur < maxValue; cur++) {
                    lockTest.count();
                }
                latch.countDown();
            }).start();
        }

        for (int i = 0; i < readThreadNum; i++) {
            new Thread(() -> {
                for (int cur = 0; cur < maxValue; cur++) {
                    lockTest.get();
                }
                latch.countDown();
            }).start();
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();

        System.out.println("LockTest执行时长：" + (endTime - startTime) + ", count" + count);

    }

    class Counter {

        public int get(){

            lock.readLock().lock();
            try {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return count;
                // System.out.println("count：" + count);
            } finally {
                lock.readLock().unlock();
            }
        }

        public void count() {
            lock.writeLock().lock();
            try {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                count++;
                // System.out.println("count：" + count);
            } finally {
                lock.writeLock().unlock();
            }
        }
    }

}
