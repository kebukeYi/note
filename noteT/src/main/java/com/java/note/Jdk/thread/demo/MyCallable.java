package com.java.note.Jdk.thread.demo;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @Author : mmy
 * @Creat Time : 2020/5/2  9:14
 * @Description
 */
public class MyCallable implements Callable<Integer> {

    private int number;

    public synchronized int vv() {
        class Helper {
            public int dd() {
                return 1;
            }
        }
        return 0;
    }

    public MyCallable(int num) {
        this.number = num;
    }

    @Override
    public Integer call() throws Exception {
        Thread.sleep(5000);
        int sum = 0;
        for (int x = 1; x <= number; x++) {
            sum += x;
        }
        System.out.println(sum);
        return sum;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<Integer> futureTask = new FutureTask<>(new MyCallable(20));
        Thread thread = new Thread(futureTask);
        thread.start();
        System.out.println(futureTask.get());
    }
}
