package com.java.note.Jdk.threadpool.one;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author : kebukeYi
 * @date :  2021-12-03 14:07
 * @description: 每个单独的任务线程(启动后就不断的获取当前任务类下的队列中的值)
 * @question:
 * @link:
 **/
public class QueryWorker implements Runnable {

    //每个线程下 单独出一个存储任务的队列
    public static LinkedBlockingQueue<QueryEntity> queryQueue = new LinkedBlockingQueue<QueryEntity>();
    //是否处于运行状态下
    public static AtomicBoolean stop = new AtomicBoolean();

    @Override
    public void run() {
        try {
            work();
        } finally {
            exit();
        }
    }

    private void work() {
        for (; ; ) {
            if (stop.get()) {
                return;
            } else {
                final QueryEntity poll;
                try {
                    poll = queryQueue.poll(1, TimeUnit.MILLISECONDS);
                    if (poll != null) {
                        dealWorker(poll);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void exit() {
    }

    public void dealWorker(QueryEntity queryEntity) {
    }

    public static void main(String[] args) {
        final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                1,
                3,
                100, TimeUnit.MILLISECONDS,
                new SynchronousQueue<>(true));

        for (int i = 0; i < 3; i++) {
            threadPoolExecutor.execute(() -> {
                for (; ; ) {
                }
            });
        }
        threadPoolExecutor.shutdown();
    }
}
 
