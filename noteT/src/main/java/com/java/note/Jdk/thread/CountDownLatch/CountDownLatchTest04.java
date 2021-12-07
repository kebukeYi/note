package com.java.note.Jdk.thread.CountDownLatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author : kebukeYi
 * @date :  2021-12-08 00:18
 * @description:
 * @question:
 * @link:
 **/
public class CountDownLatchTest04 {


    public static void main(String[] args) {
        int processors = 8;
        int totalCount = 10;
        final CountDownLatch countDownLatch = new CountDownLatch(totalCount);
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        for (int i = 0; i < 9; i++) {
            executorService.submit(() -> {
                countDownLatch.countDown();
                System.out.println("aaaaaaaaaaaaaaaaaaa");
            });
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
    }
}
 
