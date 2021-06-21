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


    public static void main(Strings[] args) throws InterruptedException {
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
 
