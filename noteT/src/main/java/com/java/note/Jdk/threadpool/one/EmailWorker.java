package com.java.note.Jdk.threadpool.one;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author : kebukeYi
 * @date :  2021-12-03 14:09
 * @description: 每个单独的任务线程(启动后就不断的获取当前任务类下的队列中的值)
 * @question:
 * @link:
 **/
public class EmailWorker implements Runnable {

    //每个线程下 单独出一个存储任务的队列
    public static LinkedBlockingQueue<EmailEntity> emailQueue = new LinkedBlockingQueue();
    //是否处于运行状态下
    public static AtomicBoolean STOP = new AtomicBoolean();

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
            if (STOP.get()) {
                return;
            } else {
                final EmailEntity poll;
                try {
                    poll = emailQueue.poll(1, TimeUnit.MILLISECONDS);
                    if (poll != null) {
                        dealWorker(poll);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void dealWorker(EmailEntity emailEntity) {
        try {
            System.out.println("处理任务");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addTask(EmailEntity emailEntity) {
        try {
            emailQueue.put(emailEntity);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void exit() {
        System.out.println("执行退出");
    }


}
 
