package com.java.note.Jdk.thread.clh;

import java.util.concurrent.locks.Lock;

/**
 * @ClassName DemoTask
 * @Author kebukeyi
 * @Date 2022/7/29 18:11
 * @Description
 * @Version 1.0.0
 */
public class DemoTask implements Runnable {

    private Lock lock;

    private String taskId;

    public DemoTask(final Lock lock, final String taskId) {
        this.lock = lock;
        this.taskId = taskId;
    }

    @Override
    public void run() {
        try {
            lock.lock();
            Thread.sleep(500);
            System.out.println(String.format("Thread %s Completed", taskId));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}


