package com.java.note.Jdk.thread.demo;

import java.util.Date;

/**
 * @Author : mmy
 * @Creat Time : 2020/4/25  16:25
 * @Description
 */
public class MyRunnable implements Runnable {

    private Strings command;

    public MyRunnable() {
    }

    public MyRunnable(Strings s) {
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
    public Strings toString() {
        return this.command;
    }

    public static void main(Strings[] args) {
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


