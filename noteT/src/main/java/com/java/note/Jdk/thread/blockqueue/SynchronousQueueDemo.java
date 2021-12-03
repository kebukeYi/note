package com.java.note.Jdk.thread.blockqueue;

import lombok.val;

import java.util.concurrent.SynchronousQueue;

/**
 * @Author : mmy
 * @Creat Time : 2020/7/21  10:07
 * @Description 同步队列 只能放一个 消费一个 ；
 * @link https://blog.csdn.net/yanyan19880509/article/details/52562039
 */
public class SynchronousQueueDemo {

    public static void main(String[] args) throws InterruptedException {
        SynchronousQueue synchronousQueue = new SynchronousQueue(true);

        final Thread thread1 = new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + "put1");
                //当没有消费者线程时  当前将会阻塞在这里
                synchronousQueue.put("1");
                //不会打印 因为当前线程已经被阻塞了
                System.out.println("1111111111111111");
                final boolean offer = synchronousQueue.offer("1");
                System.out.println(offer);
                System.out.println(Thread.currentThread().getName() + "put2");
                synchronousQueue.put("2");

                System.out.println(Thread.currentThread().getName() + "put3");
                synchronousQueue.put("3");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread1.start();

        final val thread2 = new Thread(() -> {
            try {
                //当没有消费者线程时  当前将会阻塞在这里
                synchronousQueue.put("2.2");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread2.start();
        final Thread thread = new Thread(() -> {
            try {
                Thread.sleep(3000);
                System.out.println(synchronousQueue.take());
                //System.out.println(synchronousQueue.take());
                //System.out.println(synchronousQueue.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        // thread.start();
    }
}
