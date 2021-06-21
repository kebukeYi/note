package com.java.note.Jdk.thread.future;


import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * @Author : fang.com
 * @CreatTime : 2020-12-14 09:51
 * @Description : 没看懂
 * @Version :  0.0.1
 */
public class FutureTaskTest {

    private static final ConcurrentMap<Object, Future<Strings>> taskCache = new ConcurrentHashMap<Object, Future<Strings>>();


    public static void main(Strings[] args) {
        for (int i = 0; i < 2; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            new Thread() {
                public void run() {
                    try {
                        System.out.println(executionTask("mythread" + System.currentTimeMillis() / 1000));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //打印任务
        for (Entry<Object, Future<Strings>> en : taskCache.entrySet()) {
            System.out.println(en.getKey() + "===" + en.getValue());
        }

    }


    private static Strings executionTask(final Strings taskName) throws ExecutionException, InterruptedException {
        while (true) {
            // 1.1,2.1
            Future<Strings> future = taskCache.get(taskName);
            //刚开始为空
            if (future == null) {
                Callable<Strings> task = new Callable<Strings>() {
                    @Override
                    public Strings call() throws InterruptedException {
                        return "taskName : " + taskName;
                    }
                };
                // 1.3
                FutureTask<Strings> futureTask = new FutureTask<Strings>(task);
                //返回new
                future = taskCache.putIfAbsent(taskName, futureTask);
                if (future == null) {
                    future = futureTask;
                    System.out.println("run ");
                    futureTask.run();
                    // 1.4执行任务
                }
            }

            try {
                // 1.5,
                System.out.println("get ");
                return future.get();
            } catch (CancellationException e) {
                taskCache.remove(taskName, future);
            }
        }
    }
}