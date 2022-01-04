package com.java.note.Jdk.threadpool.three;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;

import java.util.concurrent.*;

/**
 * @author : kebukeYi
 * @date :  2022-01-04 19:35
 * @description:
 * @question:
 * @link:
 **/
public class CachedThreadPoolDemo {

    static class MyRejectedExecutionHandler implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            // 可做日志记录等
            System.out.println(r + " rejected; " + " - getTaskCount: " + e.getTaskCount());
        }
    }

    static class MyThreadPoolFactory implements ThreadFactory {
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "MyThreadPoolFactory-");
        }
    }

    @Data
    @AllArgsConstructor
    static class Task implements Runnable {
        private int taskId;

        @SneakyThrows
        @Override
        public void run() {
            Thread.sleep(300);
            System.out.println("执行任务ID:" + taskId);
        }
    }

    //生产者队列(没有容量限制 容易oom)
    private final static BlockingQueue<Task> taskQueue = new LinkedBlockingQueue<>(1000);
    //正常任务队列被消费下 被拒绝的任务队列
    private final static BlockingQueue<Task> rejectTaskQueue = new LinkedBlockingQueue<>(1000);

    public ThreadPoolExecutor getCachedThreadPool(int coreSize, int maxSize) {
        return new ThreadPoolExecutor(
                coreSize,
                maxSize,
                10,
                TimeUnit.MILLISECONDS,
                new SynchronousQueue<>(),
                new MyThreadPoolFactory());
    }

    public void addTask(Task task) {
        try {
            //一直等待直到有空间
            taskQueue.put(task);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void consumerTask(int coreSize, int maxSize) {
        ThreadPoolExecutor executor = getCachedThreadPool(coreSize, maxSize);
        final Thread thread = new Thread(() -> {
            while (true) {
                Task task = null;
                try {
                    task = taskQueue.poll(10, TimeUnit.MILLISECONDS);
                    if (null == task && null == (task = rejectTaskQueue.poll(10, TimeUnit.MILLISECONDS))) {
                        continue;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    //由于是 SynchronousQueue() 因此 当提交的任务数量大于线程数量最大值时 将会抛出默认拒绝策略异常
                    executor.submit(task);
                } catch (RejectedExecutionException e) {
                    //捕捉到默认拒绝策略异常 就将任务先保存下
                    try {
                        //当发生 消费速度 <  生产速度
                        rejectTaskQueue.put(task);
                        System.out.println("线程池到达极限需要等待：" + task.getTaskId());
                        Thread.sleep(30);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "init");
        thread.start();
    }

    public void consumerRejectTask(int coreSize, int maxSize) {
        ThreadPoolExecutor executor = getCachedThreadPool(coreSize, maxSize);
        final Thread thread = new Thread(() -> {
            while (true) {
                Task task = null;
                try {
                    task = rejectTaskQueue.poll(10, TimeUnit.MILLISECONDS);
                    if (null == task && null == (task = taskQueue.poll(10, TimeUnit.MILLISECONDS))) {
                        continue;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    //由于是 SynchronousQueue() 因此 当提交的任务数量大于线程数量最大值时 将会抛出默认拒绝策略异常
                    executor.submit(task);
                } catch (RejectedExecutionException e) {
                    //捕捉到默认拒绝策略异常 就将任务先保存下
                    try {
                        //当发生 消费速度 <  生产速度
                        taskQueue.put(task);
                        System.out.println("线程池到达极限需要等待：" + task.getTaskId());
                        Thread.sleep(30);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "init");
        thread.start();
    }

    public static void main(String[] args) {
        final CachedThreadPoolDemo cachedThreadPoolDemo = new CachedThreadPoolDemo();
        final Thread producer = new Thread(() -> {
            int count = 0;
            while (true) {
                try {
                    Thread.sleep(10);
                    cachedThreadPoolDemo.addTask(new Task(count));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "producer");
        producer.start();
        int coreSize = 5;
        int maxSize = 10;
        cachedThreadPoolDemo.consumerTask(coreSize, maxSize);
        cachedThreadPoolDemo.consumerRejectTask(coreSize, maxSize);
    }
}

