package com.java.note.Jdk.thread.CountDownLatch;

import java.util.concurrent.CountDownLatch;

/**
 * @Author : mmy
 * @Creat Time : 2020/5/15  22:43
 * @Description
 */
public class CountDownLatchHttp {

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread();
            thread.join();
        }
    }

    static class AnalogUser extends Thread {
        String openId;
        CountDownLatch latch;

        public AnalogUser(String openId, CountDownLatch latch) {
            super();
            this.openId = openId;
            this.latch = latch;
        }

        @Override
        public void run() {
            try {
                //一直阻塞当前线程，直到计时器的值为0
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
