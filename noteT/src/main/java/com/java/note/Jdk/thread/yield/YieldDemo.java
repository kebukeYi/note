package com.java.note.Jdk.thread.yield;

/**
 * @author : kebukeYi
 * @date :  2021-10-01 12:46
 * @description: 使当前线程由执行状态，变成为就绪状态，让出cpu时间，在下一个线程执行时候，此线程有可能被执行，也有可能没有被执行
 * @question:
 * @link:
 **/
public class YieldDemo {

    //yield 和 sleep 的异同
    //1）yield, sleep 都能暂停当前线程，sleep 可以指定具体休眠的时间，而 yield 则依赖 CPU 的时间片划分
    //2）yield, sleep 两个在暂停过程中，如已经持有锁，则都不会释放锁资源  wait 则会释放锁资源
    //3）yield 不能被中断，而 sleep 则可以接受中断
    public static void main(String[] args) {
        Runnable runnable = () -> {
            for (int i = 0; i <= 100; i++) {
                System.out.println(Thread.currentThread().getName() + "-----" + i);
                if (i % 20 == 0) {
                    Thread.yield();
                }
            }
        };

        final Thread thread = new Thread(runnable, "栈长");
        thread.setPriority(Thread.MIN_PRIORITY);
        final Thread thread1 = new Thread(runnable, "小蜜");
        thread1.setPriority(Thread.MAX_PRIORITY);

        thread.start();
        thread1.start();
    }
}
 
