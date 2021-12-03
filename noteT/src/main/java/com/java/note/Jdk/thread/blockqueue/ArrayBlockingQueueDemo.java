package com.java.note.Jdk.thread.blockqueue;

import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author : kebukeYi
 * @date :  2021-12-03 16:16
 * @description:
 * @question:
 * @link:
 **/
public class ArrayBlockingQueueDemo {

    public static void main(String[] args) throws InterruptedException {
        //是一个数组实现的有界阻塞队列 (有界队列)，队列中元素按 FIFO
        // 排序；ArrayBlockingQueue 在创建时必须设置大小，接收的任务超出 corePoolSize 数量时，则任
        // 务被缓存到该阻塞队列中，任务缓存的数量只能为创建时设置的大小；若该阻塞队列满，则会为
        // 新的任务则创建线程，直到线程池中的线程总数> maximumPoolSize
        ArrayBlockingQueue<Integer> arrayBlockingQueueDemo = new ArrayBlockingQueue<Integer>(128);

        for (int i = 0; i < 4; i++) {
            arrayBlockingQueueDemo.put(i);
        }

        Iterator<Integer> iterator = arrayBlockingQueueDemo.iterator();

        for (int i = 0; i < 4; i++) {
            arrayBlockingQueueDemo.put(i);
        }

        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }
}
 
