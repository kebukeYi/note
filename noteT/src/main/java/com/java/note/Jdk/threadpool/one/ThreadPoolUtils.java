package com.java.note.Jdk.threadpool.one;

import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author : kebukeYi
 * @date :  2021-12-03 14:09
 * @description: for(; ;) 是精髓
 * @question: 8.如何在使用共享线程池的同时做到降低池内线程间竞争？
 * @link: https://www.processon.com/diagraming/61a86c7c1efad477b465b535
 **/
public class ThreadPoolUtils {

    //存放任务结果的
    public ArrayList<Future> queryFuture = new ArrayList<Future>();
    public ArrayList<Future> emailFuture = new ArrayList<Future>();

    AtomicInteger atomicInteger = new AtomicInteger(1);

    static ThreadPoolExecutor executor = queryThreadPool();

    private static final ThreadPoolExecutor queryThreadPool() {
        //由于使用的是 SynchronousQueue 队列
        //因此当线程池中向队列放入数据时会失败 从而走 线程池新建线程逻辑
        return new ThreadPoolExecutor(
                4,
                5,
                100, TimeUnit.MILLISECONDS,
                new SynchronousQueue<>(),
                new MyRejectedExecutionHandler());
    }

    public void submitQuery() {
        for (int i = 0; i < 3; i++) {
            //3个任务 : 每个任务线程从启动就都是不断的获取队列中的任务执行
            final QueryWorker worker = new QueryWorker();
            final Future<?> submit = executor.submit(worker);
            if (!submit.isDone()) {
                queryFuture.add(submit);
            }
        }
    }

    public void submitEmail() throws InterruptedException {
        //先投放业务性任务
        for (int i = 1; i <= 5; i++) {
            EmailWorker.emailQueue.put(new EmailEntity(i));
        }
        //创建线程池性任务
        for (int i = 1; i <= 6; i++) {
            EmailWorker worker = new EmailWorker(atomicInteger.getAndIncrement());
            Future<?> submit = executor.submit(worker);
            if (!submit.isDone()) {
                emailFuture.add(submit);
            }
            System.out.println("当前线程池中活跃线程数量：" + executor.getActiveCount());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final ThreadPoolUtils poolUtils = new ThreadPoolUtils();
        poolUtils.submitEmail();
        Thread.sleep(5000);
        //停止运行任务
        EmailWorker.STOP.set(true);
        Thread.sleep(3000);
        System.out.println("当前线程池中活跃线程数量：" + executor.getActiveCount());
        executor.shutdown();
        // poolUtils.submitQuery();
    }


}

class MyRejectedExecutionHandler implements RejectedExecutionHandler {
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
        // 可做日志记录等
        System.out.println(r + " rejected; " + " - getTaskCount: " + e.getTaskCount());
    }
}
 
