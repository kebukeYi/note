package com.java.interview.thread.wait_notify;

import com.java.interview.thread.consumer_provider.Goods;
import com.java.interview.thread.consumer_provider.IGoods;
import com.java.interview.thread.consumer_provider.Print;
import com.java.interview.thread.consumer_provider.unsafe.Consumer;
import com.java.interview.thread.consumer_provider.unsafe.Producer;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author : kebukeYi
 * @date :  2021-10-23 10:44
 * @description: 生产者消费者之间的线程间通信
 * @question:
 * @link:
 **/
public class CommunicatePetStore {

    public static final int MAX_AMOUNT = 10;

    //数据缓冲区，类定义
    static class DateBuffer<T> {
        //保存数据
        private List<T> dataList = new LinkedList<>();
        //数据缓冲区长度 volatile 一是为了避免代码重排序 二是为了可见性
        private volatile Integer amount = 0;
        private final Object LOCK_OBJECT = new Object();
        private final Object NOT_FULL = new Object();
        private final Object NOT_EMPTY = new Object();

        // 向数据区增加一个元素
        public void add(T element) throws Exception {
            //突来20个线程 跳过次代码
            while (amount > MAX_AMOUNT) {
                synchronized (NOT_FULL) {
                    Print.tcfo("队列已经满了！");
                    //等待未满通知
                    NOT_FULL.wait();
                }
            }
            //20个线程到达此处 依次获得 LOCK_OBJECT 锁
            synchronized (LOCK_OBJECT) {
                // 加上双重检查，模拟双检锁在单例模式中应用
                if (amount < MAX_AMOUNT) {
                    //可能直接加满溢出
                    dataList.add(element);
                    amount++;
                }
            }

            synchronized (NOT_EMPTY) {
                //发送未空通知
                NOT_EMPTY.notify();
            }
        }

        /**
         * 从数据区取出一个商品
         */
        public T fetch() throws Exception {
            while (amount <= 0) {
                synchronized (NOT_EMPTY) {
                    Print.tcfo("队列已经空了！");
                    //等待未空通知
                    NOT_EMPTY.wait();
                }
            }
            T element = null;
            System.out.println("Consumer trying to get LOCK_OBJECT lock ");
            //可能20个线程 一下子都来消费了
            synchronized (LOCK_OBJECT) {
                if (amount > 0) {
                    System.out.println("Consumer get LOCK_OBJECT lock ");
                    element = dataList.remove(0);
                    amount--;
                }
            }
            //
            synchronized (NOT_FULL) {
                //发送未满通知
                NOT_FULL.notify();
            }
            return element;
        }
    }

    public static void main(String[] args) {
        // Print.cfo("当前进程的 ID 是" + JvmUtil.getProcessID());
        System.setErr(System.out);
        //共享数据区，实例对象
        DateBuffer<IGoods> dateBuffer = new DateBuffer<>();
        //生产者执行的动作
        Callable<IGoods> produceAction = () -> {
            //首先生成一个随机的商品
            IGoods goods = Goods.produceOne();
            //将商品加上共享数据区
            dateBuffer.add(goods);
            return goods;
        };
        //消费者执行的动作
        Callable<IGoods> consumerAction = () -> {
            // 从 PetStore 获取商品
            IGoods goods = null;
            goods = dateBuffer.fetch();
            return goods;
        };
        // 同时并发执行的线程数
        final int THREAD_TOTAL = 21;
        //线程池，用于多线程模拟测试
        ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_TOTAL);
        //假定共 11 条线程，其中有 10 个消费者，但是只有 1 个生产者；
        final int CONSUMER_TOTAL = 1;
        final int PRODUCE_TOTAL = 20;
        for (int i = 0; i < PRODUCE_TOTAL; i++) {//生产者线程每生产一个商品，间隔 50ms
            threadPool.submit(new Producer(produceAction, 50));
        }

        for (int i = 0; i < CONSUMER_TOTAL; i++) {
            //消费者线程每消费一个商品，间隔 100ms
            threadPool.submit(new Consumer(consumerAction, 100));
        }
    }
}

 
