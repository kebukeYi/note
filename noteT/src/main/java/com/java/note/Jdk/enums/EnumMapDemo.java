package com.java.note.Jdk.enums;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * @Author : mmy
 * @Creat Time : 2020/10/18  下午 1:41
 * @Description
 */

enum color {
    GREEN, RED, BLUE, YELLOW
}

public class EnumMapDemo {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(5);
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                try {
//                    logger.info("长度:" + userScoreServicelmpl.getUserScoreTopList("aos").getData().size());
                    System.out.println("当前线程" + Thread.currentThread().getName());
                    countDownLatch.countDown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, "thread-" + i).start();
        }
        System.out.println("多线程开始并行执行......");
        countDownLatch.await();
        //   countDownLatch.countDown();
    }
}

