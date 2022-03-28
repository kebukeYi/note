package com.java.note.Jdk.thread.wait;

/**
 * @ClassName My
 * @Author kebukeyi
 * @Date 2022/3/27 19:56
 * @Description 针对实例的等待队列的操作
 * @Version 1.0.0
 */
public class MyWait implements Runnable {

    private SimpleWN simpleWN;

    public MyWait(SimpleWN simpleWN) {
        this.simpleWN = simpleWN;
    }

    public MyWait() {
    }

    public void fun1() {
        System.out.println("T1 线程执行方法1");
    }

    public void fun2() {
        System.out.println("T1 线程执行方法2");
    }

    public void fun3() {
        synchronized (simpleWN) {
            try {
                simpleWN.wait();
                System.out.println("T1 线程执行方法3");
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
