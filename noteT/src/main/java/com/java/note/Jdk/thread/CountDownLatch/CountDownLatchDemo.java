package com.java.note.Jdk.thread.CountDownLatch;

import com.java.note.Jdk.thread.CountryEnum;

import java.util.concurrent.CountDownLatch;

/**
 * @Author : mmy
 * @Creat Time : 2020/7/21  7:19
 * @Description 枚举实现
 */
public class CountDownLatchDemo {

    public static final int NUM = 10;

    public static void main(String[] args) throws InterruptedException {

    }

    public static void closeDoor() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(NUM);
        for (int i = 0; i < NUM; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + " 离开教室 ");
                countDownLatch.countDown();
            }, CountryEnum.getInstance(i) + "").start();

        }
        countDownLatch.await();
        System.out.println(Thread.currentThread().getName() + " 最后班长离开教室 ");
    }
}
