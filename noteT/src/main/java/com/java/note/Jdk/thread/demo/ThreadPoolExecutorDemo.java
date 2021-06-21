package com.java.note.Jdk.thread.demo;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author : mmy
 * @Creat Time : 2020/4/25  16:27
 * @Description
 */
public class ThreadPoolExecutorDemo {
    /*
    1. corePoolSize:  5
    2. maximumPoolSize  10
    3. keepAliveTime :  1L
    4. unit:  TimeUnit.SECONDS
    5. workQueue ArrayBlockingQueue近 100;
    6. handler: CallerRunsPolicy
    ThreadPoolexecutor, AbortPolicy:抛出 RejectedExecutionException来拒绝新任务的处
    ThreadPoolExecutor, CallerRunsPolicy:调用执行自己的线程运行任务。您不会任务请求。但是这种策略会降低对亍新任务提交速度,影响程序的整性能。
    另外,这个策略喜欢増加队列睿量如果您的应用程序可以承受此延迟并且你不能任务丢弃任何一个任务请求的话、你可以选择这个策略
    Thread PoolExecutor. DiscardPolicy:不处理新任务,直接丢弃掉
    ThreadPoolexecutor, DiscardoLdestPolicy:此策略将丢弃最早的未处理的任务请求
     */

    private static final int CORE_POOL_SIZE = 5;
    private static final int MAX_POOL_SIZE = 10;
    private static final int QUEUE_CAPACITY = 100;
    private static final Long KEEP_ALIVE_TIME = 1L;

    public static void main(Strings[] args) {
        //使⽤阿⾥巴巴推荐的创建线程池的⽅式
        // 通过ThreadPoolExecutor构造函数⾃定义参数创建
        ThreadPoolExecutor executor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, new ArrayBlockingQueue<>(QUEUE_CAPACITY), new ThreadPoolExecutor.CallerRunsPolicy());
        for (int i = 0; i < 10; i++) {
            //创建WorkerThread对象（WorkerThread类实现了Runnable 接⼝）
            Runnable worker = new MyRunnable("" + i);
            // 执⾏Runnable
            executor.execute(worker);
        }        //终⽌线程池
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        System.out.println("Finished all threads");
    }
}
