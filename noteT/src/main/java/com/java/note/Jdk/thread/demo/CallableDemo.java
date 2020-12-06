package com.java.note.Jdk.thread.demo;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @Author : mmy
 * @Creat Time : 2020/5/2  9:12
 * @Description
 */
@Slf4j
public class CallableDemo {

    private static final int CORE_POOL_SIZE = 5;
    private static final int MAX_POOL_SIZE = 10;
    private static final int QUEUE_CAPACITY = 100;
    private static final Long KEEP_ALIVE_TIME = 1L;
    /**
     * 正规做法
     */
    static ThreadPoolExecutor executor = new ThreadPoolExecutor(
            CORE_POOL_SIZE,
            MAX_POOL_SIZE,
            KEEP_ALIVE_TIME,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(QUEUE_CAPACITY),
            new ThreadPoolExecutor.CallerRunsPolicy());


    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Future<Integer> f1 = executor.submit(new MyCallable(100));
        Future<Integer> f2 = executor.submit(new MyCallable(200));
        Integer integer1 = f1.get();//当前线程会阻塞，一直等到结果返回。
        Integer integer2 = f2.get();//当前线程会阻塞，一直等到结果返回。
        System.out.println(integer1);
        System.out.println(integer2);
//        executorService.shutdown();
        executor.shutdown();
    }

    /**
     * 打印线程池的状态
     *
     * @param threadPool 线程池对象
     */
    public static void printThreadPoolStatus(ThreadPoolExecutor threadPool) {
        ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1, createThreadFactory("print-thread-pool-status", false));
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            log.info("=========================");
            log.info("ThreadPool Size: [{}]", threadPool.getPoolSize());
            log.info("Active Threads: {}", threadPool.getActiveCount());
            log.info("Number of Tasks : {}", threadPool.getCompletedTaskCount());
            log.info("Number of Tasks in Queue: {}", threadPool.getQueue().size());
            log.info("=========================");
        }, 0, 1, TimeUnit.SECONDS);
    }

    private static RejectedExecutionHandler createThreadFactory(String s, boolean b) {
        return new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {

            }
        };
    }
}
