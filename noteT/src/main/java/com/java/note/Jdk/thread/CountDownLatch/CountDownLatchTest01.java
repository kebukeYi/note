package com.java.note.Jdk.thread.CountDownLatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class CountDownLatchTest01 {

    private static final int TASK_COUNT = 8;
    private static final int THREAD_CORE_SIZE = 10;

    public static void main(String[] args) throws InterruptedException {

        Integer integer = new Integer(225);
        Integer integer2 = new Integer(225);

        System.out.println(integer == integer2);//false
        System.out.println(integer.equals(integer2));//true

        Integer integer3 = 225;
        Integer integer4 = 225;
        System.out.println(integer3 == integer4);//false
        System.out.println(integer3.equals(integer4));//true


        System.out.println("******************************************");

        CountDownLatch latch = new CountDownLatch(TASK_COUNT);

        Executor executor = Executors.newFixedThreadPool(THREAD_CORE_SIZE);
        for (int i = 0; i < TASK_COUNT; i++) {
            executor.execute(new WorkerRunnable(i, latch));
        }

        System.out.println("主线程等待所有子任务完成....");
        long mainWaitStartTimeMillis = System.currentTimeMillis();
        latch.await();
        long mainWaitEndTimeMillis = System.currentTimeMillis();
        System.out.println("主线程等待时长：" + (mainWaitEndTimeMillis - mainWaitStartTimeMillis)/1000);
    }


    static class WorkerRunnable implements Runnable {

        private int taskId;
        private CountDownLatch latch;

        @Override
        public void run() {
            doWorker();
        }

        public void doWorker() {
            System.out.println("任务ID：" + taskId + "，正在执行任务中....");
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
            } finally {
                latch.countDown();
            }
            System.out.println("任务ID：" + taskId + "，任务执行结束！");
        }

        public WorkerRunnable(int taskId, CountDownLatch latch) {
            this.taskId = taskId;
            this.latch = latch;
        }
    }
}
