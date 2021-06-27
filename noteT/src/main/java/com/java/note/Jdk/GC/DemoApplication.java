package com.java.note.Jdk.GC;

/**
 * @Author : mmy
 * @Creat Time : 2020/8/10  16:19
 * @Description 线程上下文切换
 */
public class DemoApplication {

    /**
     * 串联的执行速度比并发的执行速度要快。这就是因为线程的上下文切换导致了额外的开销，使用 Synchronized 锁关键字，导致了资源竞争，
     * 从而引起了上下文切换，但即使不使用 Synchronized 锁关键字，并发的执行速度也无法超越串联的执行速度，这是因为多线程同样存在着上下文切换。
     * Redis、NodeJS 的设计就很好地体现了单线程串行的优势。
     */

    public static void main(String[] args) {
        // 运行多线程
        MultiThreadTester test1 = new MultiThreadTester();
        test1.Start();
        // 运行单线程
        SerialTester test2 = new SerialTester();
        test2.Start();
    }


    static class MultiThreadTester extends ThreadContextSwitchTester {
        @Override
        public void Start() {
            long start = System.currentTimeMillis();
            MyRunnable myRunnable1 = new MyRunnable();
            Thread[] threads = new Thread[4];
            // 创建多个线程
            for (int i = 0; i < 4; i++) {
                threads[i] = new Thread(myRunnable1);
                threads[i].start();
            }

            for (int i = 0; i < 4; i++) {
                try {
                    // 等待一起运行完
                    threads[i].join();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            long end = System.currentTimeMillis();
            System.out.println("multi thread exce time: " + (end - start) + "s");
            System.out.println("counter: " + counter);
        }

        // 创建一个实现 Runnable 的内部类
        class MyRunnable implements Runnable {
            @Override
            public void run() {
                while (counter < 100000000) {
                    synchronized (this) {
                        if (counter < 100000000) {
                            increaseCounter();
                        }
                    }
                }
            }
        }


    }

    // 创建一个单线程
    static class SerialTester extends ThreadContextSwitchTester {
        @Override
        public void Start() {
            long start = System.currentTimeMillis();
            for (long i = 0; i < count; i++) {
                increaseCounter();
            }
            long end = System.currentTimeMillis();
            System.out.println("serial exec time: " + (end - start) + "s");
            System.out.println("counter: " + counter);
        }
    }

    // 父类
    static abstract class ThreadContextSwitchTester {

        public static final int count = 100000000;
        public volatile int counter = 0;

        public int getCount() {
            return this.counter;
        }

        public void increaseCounter() {
            this.counter += 1;
        }

        public abstract void Start();
    }

}
