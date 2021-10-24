package com.java.interview.thread.consumer_provider.unsafe;

import sun.awt.windows.ThemeReader;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author : kebukeYi
 * @date :  2021-10-21 22:53
 * @description: 通用消费者
 * @question:
 * @link:
 **/
public class Consumer implements Callable {
    //消费的时间间隔，默认等待 100 毫秒
    public static final int CONSUME_GAP = 100;
    //消费总次数
    static final AtomicInteger TURN = new AtomicInteger(0);
    //消费者对象编号
    static final AtomicInteger CONSUMER_NO = new AtomicInteger(1);
    //消费者名称
    String name;
    //消费的动作
    Callable action = null;
    //消费一次等待的时间，默认为 100ms
    int gap = CONSUME_GAP;

    public Consumer(Callable action, int gap) {
        this.action = action;
        this.gap = gap;
        name = "消费者-" + CONSUMER_NO.incrementAndGet();
    }

    @Override
    public Object call() throws Exception {
        while (true) {
            try {
                TURN.incrementAndGet();
                final Object call = action.call();
                if (call != null) {
                    System.out.println("第" + TURN.get() + "轮消费：" + call);
                }
                Thread.sleep(gap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
 
