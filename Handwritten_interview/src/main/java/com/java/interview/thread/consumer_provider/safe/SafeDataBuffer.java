package com.java.interview.thread.consumer_provider.safe;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author : kebukeYi
 * @date :  2021-10-22 14:14
 * @description: 锁粒度太大 导致 生产者 跟消费者 串行获取锁
 * @question:
 * @link:
 **/
public class SafeDataBuffer<T> {


    private static final int MAX_AMOUNT = 10;

    private List<T> dataList = new LinkedList<>();

    //保存数量
    private AtomicInteger atomicInteger = new AtomicInteger();

    //向数据区增加一个元素
    public synchronized void add(T data) throws Exception {
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

    public synchronized T fetch() throws Exception {
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
 
