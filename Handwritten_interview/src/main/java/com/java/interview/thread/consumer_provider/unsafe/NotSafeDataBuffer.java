package com.java.interview.thread.consumer_provider.unsafe;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author : kebukeYi
 * @date :  2021-10-21 22:05
 * @description:
 * @question:
 * @link:
 **/
public class NotSafeDataBuffer<T> {

    private static final int MAX_AMOUNT = 10;

    private List<T> dataList = new LinkedList<>();

    //保存数量
    private AtomicInteger atomicInteger = new AtomicInteger();

    //向数据区增加一个元素
    public void add(T data) throws Exception {
        if (atomicInteger.get() > MAX_AMOUNT) {
            System.out.println("队列已经满了");
            return;
        }
        dataList.add(data);
        atomicInteger.incrementAndGet();
        if (atomicInteger.get() != dataList.size()) {
            throw new Exception(atomicInteger.get() + "！=" + dataList.size());
        }
    }

    public T fetch() throws Exception {
        if (atomicInteger.get() <= 0) {
            System.out.println("队列已经满了");
            return null;
        }
        final T remove = dataList.remove(0);
        atomicInteger.decrementAndGet();
        if (atomicInteger.get() != dataList.size()) {
            throw new Exception(atomicInteger.get() + "！=" + dataList.size());
        }
        return remove;
    }


}
 
