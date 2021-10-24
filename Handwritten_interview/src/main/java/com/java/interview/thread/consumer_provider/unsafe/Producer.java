package com.java.interview.thread.consumer_provider.unsafe;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author : kebukeYi
 * @date :  2021-10-21 22:43
 * @description: 通用的生产者 与 生产动作 解耦
 * @question:
 * @link:
 **/
public class Producer implements Callable {

    private static final int PRODUCE_GAP = 200;

    static final AtomicInteger TURN = new AtomicInteger(0);

    //生产者对象编号
    static final AtomicInteger PRODUCER_NO = new AtomicInteger(1);

    //生产者名称
    String name = null;
    //生产的动作
    Callable action = null;

    int gap = PRODUCE_GAP;

    public Producer(Callable action, int gap) {
        this.action = action;
        this.gap = gap;
        name = "生产者-" + PRODUCER_NO.incrementAndGet();
    }


    @Override
    public Object call() throws Exception {
        while (true) {
            try {
                //执行生产动作
                Object call = action.call();
                if (call != null) {
                    System.out.println("第" + TURN.get() + "轮生产：" + call);
                }
                //每一轮生产之后，稍微等待一下
                Thread.sleep(gap);
                //增加生产轮次
                TURN.incrementAndGet();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
 
