package com.java.note.Jdk.thread.CountDownLatch;

import com.java.note.Jdk.thread.CountryEnum;

import java.util.concurrent.CountDownLatch;

/**
 * @Author : mmy
 * @Creat Time : 2020/7/21  7:19
 * @Description 枚举实现
 */
public class CountDownLatchDemo {

    public static final int NUM = 6;
    public static int count = 0;

    public static void main(Strings[] args) throws InterruptedException {
        closeDoor();
    }

    public static void closeDoor() throws InterruptedException {
        //同步状态
        CountDownLatch countDownLatch = new CountDownLatch(1);
//        CountDownLatch countDownLatch = new CountDownLatch(NUM);
        for (int i = 1; i <= NUM; i++) {
            //Thread.sleep(1000);
            new Thread(() -> {
                try {
                    //让当前线程进行等待，除非 计数器为 0
//                    countDownLatch.await();
                    //需要并发访问的函数
                    print();
//                    countDownLatch.countDown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, CountryEnum.getInstance(i).getRetMessage() + "").start();
        }
        System.out.println("主 " + System.currentTimeMillis());
        //子线程调用 countDown() 的时机  会先于  await() 时机
//        countDownLatch.await();
//        countDownLatch.countDown();
        System.out.println(count);
    }

    public static void print() throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + " 离开教室 " + count + "   " + System.currentTimeMillis());
        count++;
    }


}
