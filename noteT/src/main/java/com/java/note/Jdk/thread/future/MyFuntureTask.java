package com.java.note.Jdk.thread.future;

import lombok.SneakyThrows;

import java.util.concurrent.*;

import static java.lang.Thread.sleep;

/**
 * @Author : mmy
 * @Creat Time : 2020/8/23  下午 11:22
 * @Description
 */
public class MyFuntureTask {

    public static ExecutorService executorService = Executors.newFixedThreadPool(3);

    public static void main(String[] args) throws Exception {

        Runnable runnable = new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                System.out.println("开始睡眠5秒" + System.currentTimeMillis() / 1000);
                sleep(5000);
                System.out.println("5秒睡眠完毕" + System.currentTimeMillis() / 1000);
            }
        };

        Callable callable = new Callable() {
            @Override
            public Object call() throws Exception {
                System.out.println("开始睡眠5秒" + System.currentTimeMillis() / 1000);
                sleep(5000);
                System.out.println("5秒睡眠完毕" + System.currentTimeMillis() / 1000);
                return "OK";
            }
        };

        //callable.call();//也等待了5秒

        Future<?> future = executorService.submit(runnable);
        // Object o1 = future.get();
        // System.out.println(o1);

        FutureTask futureTask = new FutureTask(callable);
        //futureTask.run();//等待了5秒

        Future<?> submit = executorService.submit(futureTask);
        //  Object o2 = submit.get();
        // System.out.println("o2:" + o2);

        //new Thread(futureTask).start();

        System.out.println("开始睡眠3秒" + System.currentTimeMillis() / 1000);
        sleep(3000);
        System.out.println("3秒睡眠完毕" + System.currentTimeMillis() / 1000);
        Object o = futureTask.get();//等待5秒
        System.out.println("结果是：" + o);
        executorService.shutdown();
    }
}
