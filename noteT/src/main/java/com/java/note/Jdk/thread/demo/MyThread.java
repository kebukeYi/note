package com.java.note.Jdk.thread.demo;

/**
 * @ClassName MyThread
 * @Author kebukeyi
 * @Date 2022/3/27 18:27
 * @Description MyThread实例 跟 线程 不是同一个东西 ； 当线程终止了 这个实例依然不会消失；
 * @Version 1.0.0
 */
public class MyThread extends Thread {

    private String message;

    public MyThread(String message) {
        this.message = message;
    }

    @Override
    public void run() {
        System.out.println("myThread.run");
    }

    public static void main(String[] args) {
        Thread myThread = new MyThread("kkk");
        myThread.start();
    }
}
