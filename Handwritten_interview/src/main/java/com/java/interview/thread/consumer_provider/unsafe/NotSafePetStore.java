package com.java.interview.thread.consumer_provider.unsafe;

import com.java.interview.thread.consumer_provider.Goods;
import com.java.interview.thread.consumer_provider.IGoods;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author : kebukeYi
 * @date :  2021-10-21 23:07
 * @description:
 * @question:
 * @link:
 **/
public class NotSafePetStore {
    //数据缓冲区静态实例
    private static NotSafeDataBuffer<IGoods> notSafeDataBuffer = new NotSafeDataBuffer();
    //生产者执行的动作
    static Callable<IGoods> produceAction = () ->
    {
        //首先生成一个随机的商品
        IGoods goods = Goods.produceOne();
        //将商品加上共享数据区
        try {
            notSafeDataBuffer.add(goods);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return goods;
    };

    //消费者执行的动作
    static Callable<IGoods> consumerAction = () ->
    {
        // 从 PetStore 获取商品
        IGoods goods = null;
        try {
            goods = notSafeDataBuffer.fetch();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return goods;
    };

    public static void main(String[] args) {
        // 同时并发执行的线程数
        final int THREAD_TOTAL = 20;
        //线程池，用于多线程模拟测试
        ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_TOTAL);
        //必出异常
        for (int i = 0; i < 5; i++) {
            //生产者实例每生产一个商品，间隔 500ms
            threadPool.submit(new Producer(produceAction, 500));
            //消费者实例每消费一个商品，间隔 1500ms
            threadPool.submit(new Consumer(consumerAction, 1500));
        }
    }
}

 
