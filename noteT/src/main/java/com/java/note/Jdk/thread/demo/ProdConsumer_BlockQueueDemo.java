package com.java.note.Jdk.thread.demo;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author : mmy
 * @Creat Time : 2020/7/21  10:18
 * @Description 利用阻塞队列实现 消费者 生产者
 */
class ShareData2 {

    //默认是 生产+消费
    private volatile boolean flag = true;
    private AtomicInteger atomicInteger = new AtomicInteger(0);
    BlockingQueue<String> blockingDeque = null;

    //细节落地 传接口
    public ShareData2(BlockingQueue<String> blockingDeque) {
        this.blockingDeque = blockingDeque;
        System.out.println(blockingDeque.getClass().getName());
    }

    public void myProd() throws Exception {
        String data = null;
        boolean reValue = true;
        while (flag) {
            data = atomicInteger.incrementAndGet() + "";
            blockingDeque.offer(data, 2, TimeUnit.SECONDS);
            if (reValue) {
                System.out.println(Thread.currentThread().getName() + "\t 插入队列成功" + data);
            } else {
                System.out.println(Thread.currentThread().getName() + "\t 插入队列失败" + data);
            }
            Thread.sleep(1200);
        }
        System.out.println(Thread.currentThread().getName() + "\t 生产叫停");
    }


    public void myConsumer() throws Exception {
        String data = null;
        while (flag) {
            data = blockingDeque.poll(2, TimeUnit.SECONDS);
            if (data == null || data.equalsIgnoreCase("")) {
                flag = false;
                System.out.println(Thread.currentThread().getName() + "\t 消费退出" + data);
                return;
            }
            System.out.println(Thread.currentThread().getName() + "\t 消费队列成功" + data);

        }
    }

    public void stop() {
        this.flag = false;
    }
}

public class ProdConsumer_BlockQueueDemo {

    public static void main(String[] args) throws InterruptedException {
        ShareData2 shareData2 = new ShareData2(new ArrayBlockingQueue<>(10));

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t 生产线程启动");
            try {
                shareData2.myProd();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "Prod").start();

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t 消费线程启动");
            try {
                shareData2.myConsumer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "Consumer").start();

        Thread.sleep(5000);
        shareData2.stop();


    }
}
