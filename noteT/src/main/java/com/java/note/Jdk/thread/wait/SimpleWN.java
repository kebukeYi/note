package com.java.note.Jdk.thread.wait;

/**
 * @ClassName SimpleWN
 * @Author kebukeyi
 * @Date 2022/3/27 19:56
 * @Description
 * @Version 1.0.0
 */
public class SimpleWN {

    private final static SimpleWN simpleWN = new SimpleWN();

    public static void main(String[] args) throws InterruptedException {
        MyWait myWait = new MyWait(simpleWN);
        MyNotify myNotify = new MyNotify(simpleWN);
        Thread thread = new Thread(myWait);
        thread.start();
        //静态方法
        //暂停的不是 thread 相关的线程 而是执行调用 sleep 的线程
        thread.sleep(1000);
        new Thread(myNotify).start();
    }
}
