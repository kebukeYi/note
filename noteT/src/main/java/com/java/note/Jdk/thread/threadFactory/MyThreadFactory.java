package com.java.note.Jdk.thread.threadFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * @ClassName MyThreadFactory
 * @Author kebukeyi
 * @Date 2022/3/27 18:54
 * @Description 如何唤醒sleep状态下的线程？
 * @Version 1.0.0
 */
public class MyThreadFactory {

    public static void main(String[] args) throws InterruptedException {
        ThreadFactory defaultThreadFactory = Executors.defaultThreadFactory();
        Thread newThread = defaultThreadFactory.newThread(() -> {
            try {
                // 默认线程进行睡眠(不会释放自己所拥有的互斥锁)时会抛出异常机制(可利用此处的异常机制来当作 唤醒机制)
                // 当在他处对此线程进行中断请求后
                // 那么当前此处线程会立即抛出 InterruptedException 异常 (被唤醒了) 并且重置中断位
                // 需要在catch 处进行捕获处理
                System.out.println("线程工厂创建线程");
                Thread.sleep(7000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        newThread.start();
        Thread.sleep(1000);
        newThread.interrupt();
        Thread.sleep(1000 * 5);
        System.out.println("main is over ");
    }
}
