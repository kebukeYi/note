package com.java.note.Jdk.thread.demo;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class DemoTest {

    private static volatile int x = 0;
    private static AtomicInteger atomicInteger = new AtomicInteger(0);
    private final static int count = 10000;
    private static Object lock = new Object();


    public static void test01() throws InterruptedException {
        ThreadDemo threadDemo = new ThreadDemo();
        threadDemo.start();
        threadDemo.wait();

        CountDownLatch cdl = new CountDownLatch(count);
        for (int i = 0; i < count; i++) {
            new Thread(() -> {
                // 线程休眠 统一休眠 加大之后的并发+1操作
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (lock) {
                    x++;
                }
                cdl.countDown();
                atomicInteger.incrementAndGet();
            }).start();
        }
        cdl.await();
        System.out.println(x);
        System.out.println(atomicInteger);
    }

    public static void main(String[] args) throws InterruptedException {
        test01();
    }

    static class ThreadDemo extends Thread {

        @Override
        public void run() {
            super.run();
        }
    }


}
