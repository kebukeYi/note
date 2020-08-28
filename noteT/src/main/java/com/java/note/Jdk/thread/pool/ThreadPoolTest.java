package com.java.note.Jdk.thread.pool;

import java.util.Random;
import java.util.concurrent.*;

/**
 * @Author : mmy
 * @Creat Time : 2020/5/10  7:08
 * @Description
 */
public class ThreadPoolTest {


    // 任务类
    static class MyTask implements Runnable {

        private LinkedBlockingDeque<String> linkedBlockingDeque;
        private CountDownLatch countDownLatch;
        private String name;
        private int channelId;
        private int count = 0;
        private Random r = new Random();

        public MyTask(LinkedBlockingDeque<String> linkedBlockingDeque, String name, int channelId) {
            this.channelId = channelId;
            this.linkedBlockingDeque = linkedBlockingDeque;
            this.name = name;
        }

        public MyTask(CountDownLatch countDownLatch, String name) {
            this.countDownLatch = countDownLatch;
            this.name = name;
        }

        public MyTask() {
        }


        public String getName() {
            return name;
        }

        @Override
        public void run() {// 执行任务
            try {
//                while (count < 10) {
                //必要时阻塞
                String str = linkedBlockingDeque.take();
                System.out.println("str: " + str + (count++));
//                }
                System.out.println(Thread.currentThread().getName() + " 任务" + name + " 完成");
//                Thread.sleep(1000);
//                countDownLatch.countDown();
            } catch (Exception e) {
                System.out.println(Thread.currentThread().getId() + " sleep InterruptedException:"
                        + Thread.currentThread().isInterrupted());
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
//        executor.execute(new MyTask());
//        executor.submit(new MyTask());
        // 创建3个线程的线程池
        MyThreadPoolManager t = new MyThreadPoolManager(3);
        CountDownLatch countDownLatch = new CountDownLatch(5);
        LinkedBlockingDeque<String> linkedBlockingDeque = new LinkedBlockingDeque<>();
        linkedBlockingDeque.add("A");
        linkedBlockingDeque.add("B");
        linkedBlockingDeque.add("C");
        for (int i = 0; i < 3; i++) {
            t.excute(new MyTask(linkedBlockingDeque, "testA", i));
        }

//        t.excute(new MyTask(countDownLatch, "testB"));
//        t.excute(new MyTask(countDownLatch, "testC"));
//        t.excute(new MyTask(countDownLatch, "testD"));
//        t.excute(new MyTask(countDownLatch, "testE"));

//        countDownLatch.await();
        Thread.sleep(1000);
        t.destroy();// 所有线程都执行完成才destory

    }

}
