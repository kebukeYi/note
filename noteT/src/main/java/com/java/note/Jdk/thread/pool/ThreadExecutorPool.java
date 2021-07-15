package com.java.note.Jdk.thread.pool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.IntStream;

/**
 * @Author : mmy
 * @Creat Time : 2021年7月13日12:49:36
 * @Description yes哥
 */
public class ThreadExecutorPool {

    BlockingQueue<Runnable> taskQueue;
    List<YMThread> workers;


    public ThreadExecutorPool(BlockingQueue<Runnable> taskQueue, int coreSize) {
        this.taskQueue = taskQueue;
        this.workers = new ArrayList<>(coreSize);
        IntStream.rangeClosed(1, coreSize).forEach((i) -> {
            final YMThread ymThread = new YMThread("YMThread_task_" + i);
            ymThread.start();
            workers.add(ymThread);
        });
    }

    public void execute(Runnable runnable)   {
//        taskQueue.add(runnable);
        taskQueue.offer(runnable);
//        taskQueue.put(runnable);
    }

    class YMThread extends Thread {

        public YMThread(String name) {
            super(name);
        }

        public void run() {
            while (true) {
                try {
                    final Runnable take = taskQueue.take();
                    take.run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void main(String[] args) {
        final ThreadExecutorPool threadExecutorPool = new ThreadExecutorPool(new LinkedBlockingQueue<>(10), 3);
        IntStream.rangeClosed(0, 10).forEach(i -> {
            threadExecutorPool.execute(() -> {
                System.out.println(Thread.currentThread().getName() + " very easy thread pool excete");
            });
        });

    }
}


