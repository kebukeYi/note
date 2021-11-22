package com.java.note.Jdk.thread.longadder;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author : kebukeYi
 * @date :  2021-11-22 14:43
 * @description:
 * @question:
 * @link:
 **/
public class MyLongAdderDemo {

    private static MyLongAdder count = new MyLongAdder();
    private static Integer MAX_COUNT = 1000000;
    private static CountDownLatch countDownLatch = new CountDownLatch(4);

    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();
        for (int i = 1; i <= 4; i++) {
            new Thread(() -> {
                for (Integer integer = 0; integer < MAX_COUNT; integer++) {
                    count.increment();
                }
                countDownLatch.countDown();
            }, i + "").start();
        }
        countDownLatch.await();
        System.out.println(count.sum() + " 花费了：" + (System.currentTimeMillis() - start));
    }
}
 
