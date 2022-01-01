package com.thread.affinity;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.*;

/**
 * @author : kebukeYi
 * @date :  2022-01-01 22:10
 * @description:
 * @question:
 * @link:
 **/
public class Demo {

    //生产者队列(没有容量限制 容易oom)
    final static BlockingQueue<Task> linkedBlockingQueue = new LinkedBlockingQueue<>();

    //消费者线程池
    final static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            10,
            30,
            100, TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<>(1024),
            new MyThreadFactory(),
            new MyRejectedExecutionHandler());

    public static void taskTask() {
        while (true) {
            //会阻塞
            final Task task;
            try {
                task = linkedBlockingQueue.poll(10, TimeUnit.MILLISECONDS);
                if (task != null) {
                    threadPoolExecutor.submit(task);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public static void main(String[] args) throws InterruptedException {
        linkedBlockingQueue.put(new Task());
        Demo.taskTask();
    }
}

class MyThreadFactory implements ThreadFactory {
    @Override
    public Thread newThread(@NotNull Runnable r) {
        return new Thread("Thread-Name-");
    }
}

class MyRejectedExecutionHandler implements RejectedExecutionHandler {
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
        // 可做日志记录等
        System.out.println(r + " rejected; " + " - getTaskCount: " + e.getTaskCount());
    }
}

class Task implements Runnable {
    @Override
    public void run() {
        System.out.println("执行任务");
    }
}


