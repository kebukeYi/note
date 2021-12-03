package com.java.note.Jdk.threadpool.one;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author : kebukeYi
 * @date :  2021-12-03 14:09
 * @description:
 * @question:
 * @link:
 **/
public class EmailWorker implements Runnable {

    public static LinkedBlockingQueue<EmailEntity> emailQueue = new LinkedBlockingQueue();
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

    }

    private void exit() {

    }


}
 
