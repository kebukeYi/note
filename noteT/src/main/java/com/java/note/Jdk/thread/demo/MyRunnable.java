package com.java.note.Jdk.thread.demo;

import java.util.Date;

/**
 * @Author mmy
 * @CreatTime 2020/4/25  16:25
 * @Description
 */
public class MyRunnable implements Runnable {

    private String command;

    public MyRunnable() {
    }

    public MyRunnable(String s) {
        this.command = s;
    }

    @Override
    public void run() {
        int i = 0;
        while (i < 100) {
            i++;
            System.out.println(Thread.currentThread().getName() + " Start. Time = " + new Date());
            processCommand(i);
            System.out.println(Thread.currentThread().getName() + " End. Time = " + new Date());
        }

    }

    private void processCommand(int i) {
        try {
            System.out.println(i);
            Thread.sleep(500);
        } catch (InterruptedException e) {
            // 判断该阻塞线程是否还在
            System.out.println(Thread.currentThread().isAlive());
            // 判断该线程的中断标志位状态
            System.out.println(Thread.currentThread().isInterrupted());
            System.out.println("In Runnable");
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return this.command;
    }

    //main 方法还真只能写在 class 内部
    public static void main(String[] args) {
        MyRunnable myRunnable = new MyRunnable();
        Thread thread = new Thread(myRunnable);
        thread.start();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            System.out.println("Main");
        }
        thread.interrupt();
    }

}

