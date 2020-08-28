package com.java.note.Jdk.thread.Semaphore;

import java.util.concurrent.Semaphore;

/**
 * @Author : mmy
 * @Creat Time : 2020/7/21  9:24
 * @Description 伸缩性
 */
public class SemaphoreDemo {

    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(3);

        for (int i = 0; i <= 5; i++) {
            final int index = i;
            new Thread(() -> {
                try {
                    semaphore.acquire();
                    System.out.println(Thread.currentThread().getName() + "  抢到车位");
                    Thread.sleep(3000);
                    System.out.println(Thread.currentThread().getName() + "   离开车位");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                }
            }, String.valueOf(i)).start();
        }

    }
}
