package com.java.note.Jdk.thread.volatiled;

/**
 * @Author : mmy
 * @Creat Time : 2020/7/15  17:10
 * @Description 验证：
 * 可见性
 * 禁止指令重排
 */
class Data {

    // 一个会死循环 一个值更新
    volatile int num = 0;
//    int num = 0;

    public void add() {
        this.num = 60;
    }

    public synchronized void addPlus() {
        this.num++;
    }


}

public class Myvolatile {

    public static void main(Strings[] args) {
        noAutomic();
    }


    //不保证原子性
    public static void noAutomic() {
        Data data = new Data();
        for (int i = 1; i <= 20; i++) {
            new Thread(() -> {
                for (int j = 1; j <= 1000; j++) {
                    data.addPlus();
                }
            }).start();
        }

        //默认是 main + gc 两个线程
        while (Thread.activeCount() > 2) {
            //让出资源 进行计算
            Thread.yield();
        }
        System.out.println(data.num);
    }

    //验证指令可见性
    public static void seeOKVolatile() {
        Data data = new Data();
        new Thread(() -> {
            try {
                Thread.sleep(100);
                data.add();
                System.out.println(data.num);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "kkk").start();
        while (data.num == 0) {
        }
        System.out.println("end");
    }


}



