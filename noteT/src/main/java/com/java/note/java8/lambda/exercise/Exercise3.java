package com.java.note.java8.lambda.exercise;

import java.util.concurrent.Callable;

/**
 * @Author : mmy
 * @Creat Time : 2020/3/31  21:51
 * @Description
 */
public class Exercise3 {


    public static void main(String[] args) {
//开辟线程




    }


    //开辟线程
    public static void threads(String[] args) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("你好");
            }
        });
        thread.start();

        Thread thread1 = new Thread(() -> {
            System.out.println("还行");
        });
        thread1.start();

        Callable callable = new Callable() {
            @Override
            public Object call() {
                return "放放风";
            }
        };
        try {
            Object o = callable.call();
            System.out.println(o.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Runnable runnable = () -> System.out.println("run1");
        runnable.run();
        Object object = runnable;
        //0bject objLambda= () -> System. out. println("run")报错, 必须转化为-一个函数式接口
        Object obj = (Runnable) () -> System.out.println("run2");
    }

}
