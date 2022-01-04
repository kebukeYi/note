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

    //模拟生产者生产
    public void addTask(Task task) {
        try {
            //一直等待直到有空间
            taskQueue.put(task);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //消费者消费正常生产出的消息
    public void consumerTask(int coreSize, int maxSize) {
        //创建可缓存线程池
        ThreadPoolExecutor executor = getCachedThreadPool(coreSize, maxSize);
        final Thread thread = new Thread(() -> {
            //死循环进行消费消息
            while (true) {
                Task task = null;
                try {
                    //先从正常队列中消费消息
                    task = taskQueue.poll(10, TimeUnit.MILLISECONDS);
                    //当 taskQueue 中没有消息时 尝试从 rejectTaskQueue 队列中获得消息
                    if (null == task && null == (task = rejectTaskQueue.poll(10, TimeUnit.MILLISECONDS))) {
                        //都没有消息的话 继续循环
                        continue;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    //由于是 SynchronousQueue() 因此 当提交的任务数量大于线程数量的最大值时 将会抛出默认拒绝策略异常
                    executor.submit(task);
                } catch (RejectedExecutionException e) {
                    //捕捉到默认拒绝策略异常 就将任务先保存到 拒绝队列中
                    try {
                        //当前消费不到 就放到另外一个对列中
                        rejectTaskQueue.put(task);
                        System.out.println("当前线程池到达极限 需要等待：" + task.getTaskId());
                        //当前 consumerTask 线程 等待 线程池中的线程进行消费消息
                        Thread.sleep(30);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "consumerTask");
        thread.start();
    }

    //消费者消费曾被拒绝消费的消息
    public void consumerRejectTask(int coreSize, int maxSize) {
        //创建可缓存线程池
        ThreadPoolExecutor executor = getCachedThreadPool(coreSize, maxSize);
        final Thread thread = new Thread(() -> {
            //当前线程死循环的消费任消息
            while (true) {
                Task task = null;
                try {
                    //从 拒绝队列中消费消息
                    task = rejectTaskQueue.poll(10, TimeUnit.MILLISECONDS);
                    // //当 rejectTaskQueue 中没有消息时 尝试从 taskQueue 队列中获得消息
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
                        //当前消费不了 就放到另外一个对列中
                        taskQueue.put(task);
                        System.out.println("线程池到达极限需要等待：" + task.getTaskId());
                        //当前 consumerRejectTask 线程等待 线程池中的线程进行消费消息
                        Thread.sleep(30);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "consumerRejectTask");
        thread.start();
    }

    public static void main(String[] args) {
        final CachedThreadPoolDemo cachedThreadPoolDemo = new CachedThreadPoolDemo();
        //模拟生产者 生产消息
        final Thread producer = new Thread(() -> {
            int count = 0;
            while (true) {
                try {
                    Thread.sleep(10);
                    //不停的生产消息
                    cachedThreadPoolDemo.addTask(new Task(count));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "producer");
        producer.start();
        int coreSize = 5;
        int maxSize = 10;
        //消费正常生产出的消息
        cachedThreadPoolDemo.consumerTask(coreSize, maxSize);
        //消费曾被拒绝消费的消息
        cachedThreadPoolDemo.consumerRejectTask(coreSize, maxSize);
    }
}

