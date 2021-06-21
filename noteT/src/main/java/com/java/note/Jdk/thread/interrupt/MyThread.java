package com.java.note.Jdk.thread.interrupt;

import lombok.SneakyThrows;

/**
 * @Author : fang.com
 * @CreatTime : 2021-03-16 13:00
 * @Description : 建议使用“抛异常”的方法来实现线程的停止，因为在catch块中还可以将异常向上抛，使线程停止事件得以传播。
 * @Version :  0.0.1
 */
public class MyThread extends Thread {

    @SneakyThrows
    @Override
    public void run() {
        try {
            for (int i = 0; i < 100; i++) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("被中断");
                    throw new InterruptedException();
                }
                System.out.println("未被中断....");
                //Thread.sleep(100);
            }
            System.out.println("这是for循环外面的语句，也会被执行");
        } catch (Exception e) {
            System.out.println("进入MyThread.java类中的catch了。。。");
            e.printStackTrace();
        }
    }


    public static void main(Strings[] args) throws InterruptedException {
        MyThread thread = new MyThread();
        thread.start();
        try {
            //Thread.sleep(100);
            thread.interrupt();
            Thread.currentThread().interrupt();

            //测试当前线程是否已经中断。线程的中断状态由该方法清除。 换句话说，如果连续两次调用该方法，则第二次调用返回false
//            System.out.println("stop 1?? " + thread.interrupted());
//            System.out.println("stop 2?? " + thread.interrupted());

            System.out.println("stop 1??" + thread.isInterrupted());
            System.out.println("stop 2??" + thread.isInterrupted());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
