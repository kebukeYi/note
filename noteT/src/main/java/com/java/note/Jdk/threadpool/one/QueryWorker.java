package com.java.note.Jdk.threadpool.one;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author : kebukeYi
 * @date :  2021-12-03 14:07
 * @description:
 * @question:
 * @link:
 **/
public class QueryWorker implements Runnable {

    public static LinkedBlockingQueue<QueryEntity> queryQueue = new LinkedBlockingQueue<QueryEntity>();
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
 
