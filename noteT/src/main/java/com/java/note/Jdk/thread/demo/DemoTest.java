package com.java.note.Jdk.thread.demo;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class DemoTest {

    private static int x = 0;
    private static volatile int y = 0;
    private static AtomicInteger atomicInteger = new AtomicInteger(0);
    private final static int count = 100;
    private static Object lock = new Object();


    public static void test01() throws InterruptedException {
        CountDownLatch cdl = new CountDownLatch(count);

        for (int i = 0; i < count; i++) {
            new Thread(() -> {
                // 线程休眠 统一休眠 加大之后的并发+1操作
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //同步执行
//                synchronized (lock) {
//                    x++;
//                }
                //
                y++;
                //这个是保证原子性吗？
                cdl.countDown();
                //这个是原子性
                atomicInteger.incrementAndGet();
            }).start();
        }//for结束

        //主线程进行阻塞
        cdl.await();
        System.out.println("x " + x);
        System.out.println("y " + y);
        System.out.println("atomicInteger " + atomicInteger.get());
    }

    public static void main(String[] args) throws InterruptedException {
        test01();
    }
}

class ThreadDemo extends Thread {
    @Override
    public void run() {
        super.run();
    }
}

