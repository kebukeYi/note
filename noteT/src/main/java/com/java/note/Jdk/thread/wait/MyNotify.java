package com.java.note.Jdk.thread.wait;

/**
 * @ClassName MyNotify
 * @Author kebukeyi
 * @Date 2022/3/27 20:00
 * @Description
 * @Version 1.0.0
 */
public class MyNotify implements Runnable {

    private SimpleWN simpleWN;

    public MyNotify(SimpleWN simpleWN) {
        this.simpleWN = simpleWN;
    }


    public void fun1() {
        System.out.println("T2 线程执行方法1");
    }

    public void fun2() {
        System.out.println("T2 线程执行方法2");
    }

    public void fun3() {
        synchronized (simpleWN) {
            try {
                System.out.println("T2 线程执行方法3");
                Thread.sleep(3000);
                //执行后 并不会马上唤醒线程 因为此时还未释放锁
                //至于释放哪个线程
                simpleWN.notify();
                //将所有的线程唤醒 来进行争抢当前线程获得的 notifyAll 的锁
                simpleWN.notifyAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        fun1();
        fun2();
        fun3();
    }
}
