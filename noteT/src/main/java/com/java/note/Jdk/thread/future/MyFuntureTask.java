package com.java.note.Jdk.thread.future;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @Author : mmy
 * @Creat Time : 2020/8/23  下午 11:22
 * @Description
 */
public class MyFuntureTask {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Callable callable = new Callable() {
            @Override
            public Object call() throws Exception {
                return null;
            }
        };
        FutureTask futureTask = new FutureTask(callable);
        futureTask.run();
        futureTask.get();
    }
}
