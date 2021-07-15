package com.java.note.Jdk.thread.pool;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author : mmy
 * @Creat Time : 2020/5/9  22:10
 * @Description
 */
public class MyThreadPoolManager implements MyThreadPool {

    /**
     * 线程池数量
     */
    private static int workerNumber = 5;

    /**
     * 工作的线程工人
     */
    private Worker[] worker;

    /**
     * 完成任务数，volatile表示线程可见
     */
    private static volatile int taskNums = 0;

    /**
     * 任务队列 先进先出 linkedlist的 头尾插入删除效率高
     */
    private static BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<Runnable>();

    /**
     * 由于是非单例模式，确保每次创建的线程原子性 用AtomicInteger也可
     */
    private AtomicLong atom = new AtomicLong();

    /**
     * Facade模式，最终由instanceMyThreadPool()方法生产MyThreadPool对象
     */
    private MyThreadPoolManager() {
        this(workerNumber);
    }

    public MyThreadPoolManager(int workerNumber) {
        //参数判断 小于等于0 则为默认数量
        if (workerNumber > 0) {
            MyThreadPoolManager.workerNumber = workerNumber;
        }

        //初始化线程池
        this.worker = new Worker[MyThreadPoolManager.workerNumber];
        for (int i = 0; i < MyThreadPoolManager.workerNumber; i++) {
            worker[i] = new Worker();
            worker[i].setName("worker-" + atom.incrementAndGet());
            worker[i].start();
        }
    }

    /**
     * 初始化线程池
     *
     * @return
     */
    public static MyThreadPool instanceMyThreadPool() {
        return new MyThreadPoolManager();
    }

    public static MyThreadPool instanceMyThreadPool(int poolSize) {
        return new MyThreadPoolManager(poolSize);
    }

    /**
     * 执行任务 由于采用了LinkedList而非  CopyOnWriteArrayList  所以需要对任务进行同步 防止并发
     */
    @Override
    public void excute(Runnable task) {
//        synchronized (task) {   taskQueue.put(task); //会阻塞
        synchronized (taskQueue) {
            taskQueue.add(task);
            //提交任务后唤醒等待在队列的线程
            taskQueue.notifyAll();
        }
    }

    /**
     * 在线程池准备好了后，我们需要像线程池中提交工作任务，任务统一提交到队列中，当有任务时，自动分发线程。
     *
     * @param tasks
     */
    @Override
    public void excute(Runnable[] tasks) {
        synchronized (tasks) {
            taskQueue.addAll(Arrays.asList(tasks));
            taskQueue.notifyAll();
//            this.notifyAll();
        }
    }

    @Override
    public void excute(List<Runnable> tasks) {
        synchronized (tasks) {
            taskQueue.addAll(tasks);
            taskQueue.notifyAll();
//            this.notifyAll();
        }
    }

    @Override
    public int getThreadTaskNum() {
        return taskQueue.size();
    }

    @Override
    public int getThreadWorkerNum() {
        // TODO Auto-generated method stub
        return worker != null ? worker.length : 0;
    }

    @Override
    public int getFinishedTaskNum() {
        // TODO Auto-generated method stub
        return taskNums;
    }

    /**
     * 销毁线程池，线程池的重点，画圈
     */
    @Override
    public void destroy() {
        //有任务的情况下不销毁，进入等待
        //为何这里不需要采用同步，实践出来的
        //在多线程下，避开这里的等待，进入下方的清空worker后，清空线程的同时，并发的线程已经在执行中了，所以不需要同步
        while (taskQueue.size() > 0) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //没任务的情况下
        for (Worker a : worker) {
            //调用关闭方法
            a.stopWork();
            //设置为空，手动释放资源
            a = null;
        }
        //设置为空，手动释放资源
        worker = null;
        System.out.println("finished...");
    }


    /**
     * 核心Worker类 是线程池执行 任务 的类
     *
     * @author
     */
    private class Worker extends Thread {

        /**
         * 是否工作状态 为false 则不工作; 为了销毁使用
         */
        private boolean isWork = true;

        @Override
        public void run() {
            Runnable r = null;
            while (isWork && !isInterrupted()) {
                //同步任务队列 防止并发抢占
                synchronized (taskQueue) {
                    //任务队列为空的时候则进入等待 为何不用wait（）？
                    //wait（）方法在此中只会有一个线程进入等待。在外部唤醒的时候必须调用该对象的notify()方法才能唤醒
                    //而外部则为notifyAll()
                    while (isWork && taskQueue.isEmpty()) {
                        try {
                            //Thread.sleep(20); 不释放锁
                            taskQueue.wait(1000);//释放了锁
                            System.out.println(Thread.currentThread().getName()+"  执行了wait  ");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    //取值 运行任务 计数器+1 把任务置空
                    if (isWork && !isInterrupted() && !taskQueue.isEmpty()) {
                        try {
                            //  r = taskQueue.take();
                            r = taskQueue.remove();
                        } catch (Exception e) {
                            e.printStackTrace();
                            r = null;
                        }
                    }
                    //判断任务是否为空 防止异常
                    if (r != null) {
                        r.run();//任务队列中的run方法
                        taskNums++;
                    }
                    r = null;
                }
            }
        }

        /**
         * 关闭work
         */
        private void stopWork() {
            isWork = false;
        }
    }

}
