package com.java.note.Jdk.thread.blockqueue;

import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author : kebukeyi
 * @date :  2021-05-15 10:16
 * @description :
 * @question :
 * @usinglink :
 **/
public class LinkedBlockingQueueDemo {

    public static void main(String[] args) throws InterruptedException {
        //是一个基于链表实现的阻塞队列，按 FIFO 排序任务，可以设置
        // 容量(有界队列)，不设置容量则默认使用 Integer.Max_VALUE 作为容量 （无界队列）。该队列的
        // 吞吐量高于 ArrayBlockingQueue
        //如果不设置 LinkedBlockingQueue 的容量（无界队列），当接收的任务数量超出 corePoolSize
        // 数量时，则新任务可以被无限制地缓存到该阻塞队列中，直到资源耗尽。有两个快捷创建线程池
        // 的工厂方法 Executors.newSingleThreadExecutor、Executors.newFixedThreadPool，使用了这个队列，
        // 并且都没有设置容量（无界队列）
        LinkedBlockingQueue<Integer> blockingQueue = new LinkedBlockingQueue<>();

        for (int i = 0; i < 4; i++) {
            blockingQueue.put(i);
        }

        Iterator<Integer> iterator = blockingQueue.iterator();

        for (int i = 0; i < 4; i++) {
            blockingQueue.put(i);
        }

        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }

    }
}
 
