package com.java.note.Jdk.queue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Author : mmy
 * @Creat Time : 2020/7/21  9:34
 * @Description 实现
 * 当阻寒队团是空时, 从队列中获取元素的操作将会被阻塞。
 * 当阻塞队列是满时,往队列里添加元素的操作将会被阻塞。
 */
public class BlockingQueueDemo {


    public static void main(Strings[] args) throws InterruptedException {
        BlockingQueue arrayBlockingQueue = new ArrayBlockingQueue(3);
        arrayBlockingQueue.add("a");
        arrayBlockingQueue.add("b");
        arrayBlockingQueue.add("c");
        arrayBlockingQueue.offer("c");
        arrayBlockingQueue.put("f");

        System.out.println(arrayBlockingQueue.peek());
        System.out.println(arrayBlockingQueue.take());
        System.out.println(arrayBlockingQueue.size());

        BlockingQueue linkedBlockingQueue = new LinkedBlockingQueue();
    }


}
