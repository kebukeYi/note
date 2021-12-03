package com.java.note.Jdk.threadpool.one;

import java.util.ArrayList;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author : kebukeYi
 * @date :  2021-12-03 14:09
 * @description: for(; ;) 是精髓
 * @question: 8.如何在使用共享线程池的同时做到降低池内线程间竞争？
 * @link: https://www.processon.com/diagraming/61a86c7c1efad477b465b535
 **/
public class ThreadPoolUtils {

    public ArrayList<Future> queryFuture = new ArrayList<Future>();
    public ArrayList<Future> emailFuture = new ArrayList<Future>();

    static ThreadPoolExecutor executor = queryThreadPool();

    private static final ThreadPoolExecutor queryThreadPool() {
        //由于使用的是 SynchronousQueue 队列
        //因此当线程池中向队列放入数据时会失败 从而走 新建线程逻辑
        return new ThreadPoolExecutor(
                4,
                5,
                100, TimeUnit.MILLISECONDS,
                new SynchronousQueue<>());
    }

    public void submitQuery() {
        for (int i = 0; i < 3; i++) {
            final QueryWorker worker = new QueryWorker();
            final Future<?> submit = executor.submit(worker);
            if (!submit.isDone()) {
                queryFuture.add(submit);
            }
        }
    }

    public void submitEmail() {
        for (int i = 0; i < 3; i++) {
            final EmailWorker worker = new EmailWorker();
            final Future<?> submit = executor.submit(worker);
            if (!submit.isDone()) {
                emailFuture.add(submit);
            }
        }
    }

    public static void main(String[] args) {
        final ThreadPoolUtils poolUtils = new ThreadPoolUtils();
        poolUtils.submitEmail();
        poolUtils.submitQuery();
    }


}
 
