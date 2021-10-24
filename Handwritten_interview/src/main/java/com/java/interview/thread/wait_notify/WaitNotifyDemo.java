package com.java.interview.thread.wait_notify;

import java.util.Scanner;

/**
 * @author : kebukeYi
 * @date :  2021-10-22 18:10
 * @description: “等待-通知”通信模式演示示例
 * @question:
 * @link:
 **/
public class WaitNotifyDemo {

    private static Object lock = new Object();

    static String string = null;

    static class WaitThread implements Runnable {
        @Override
        public void run() {
            synchronized (lock) {
                try {
                    //启动等待
                    System.out.println("启动等待");
                    //等待被通知，同时释放 locko 监视器的 Owner 权限
                    lock.wait();
                    System.out.println(string);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //收到通知后，线程会进入 locko 监视器的 EntryList
                System.out.println("收到通知，当前线程继续执行 " + string);
            }
        }
    }

    static class NotifyTarget implements Runnable {
        @Override
        public void run() {
            synchronized (lock) {
                try {
                    //从屏幕读取输入，目的阻塞通知线程，方便使用 jstack 查看线程状态
                    string = new Scanner(System.in).nextLine();
                    //获取 lock 锁，然后进行发送
                    // 此时不会立即释放 locko 的 Monitor 的 Owner，需要执行完毕
                    lock.notify();
                    System.out.println("发出通知了，但是线程还没有立马释放锁");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final Thread thread = new Thread(new WaitThread(), "WaitThread");
        thread.start();
        Thread.sleep(5000);
        //创建通知线程
        Thread notifyThread = new Thread(new NotifyTarget(), "NotifyThread");
        //启动通知线程
        notifyThread.start();
    }


}
 
