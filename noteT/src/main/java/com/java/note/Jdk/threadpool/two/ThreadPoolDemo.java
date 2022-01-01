package com.java.note.Jdk.threadpool.two;

/**
 * @author : kebukeYi
 * @date :  2022-01-02 00:37
 * @description: 两种实现有什么不一样的吗?
 * @question:
 * @link:
 **/
public class ThreadPoolDemo {

    public static void main(String[] args) throws InterruptedException {
        //新建线程 并且创建用户任务
        final Work work = new Work("one", () -> {
            System.out.println("A用户任务被当前线程 " + Thread.currentThread().getName() + " 执行 ");
        });
        work.thread.start();
        work.thread.interrupt();
        Thread.sleep(3 * 1000);

        System.out.println("====================================");

        //新建线程 并且创建用户任务
        final Worker worker = new Worker("two", () -> {
            System.out.println("B用户任务被当前线程 " + Thread.currentThread().getName() + " 执行 ");
        });
        worker.run();
        worker.thread.interrupt();
        Thread.sleep(3 * 1000);
    }
}

//原生Jdk的实现 实现了 Runnable 接口
class Work implements Runnable {
    Runnable firstTask;
    String name;
    Thread thread;

    public Work(String name, Runnable runnable) {
        this.name = name;
        this.firstTask = runnable;
        //将Worker自身也就是this，传入了进去，也就是说最后创建出来的线程对象，它里面的target属性就是指向这个Worker对象
        //调用 work.thread.start(); 时 就会执行 target 的 run() 方法
        this.thread = new Thread(this, name);
    }

    @Override
    public void run() {
        runWorker(this);
    }

    private void runWorker(Work work) {
        if (work.firstTask != null) {
            work.firstTask.run();
        }
        while (true) {
            try {
                Thread.sleep(2 * 1000);
                System.out.println(name + "线程从任务队列中获取任务");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

//自定义实现 并且 没有实现 Runnable 接口
class Worker {
    Runnable firstTask;
    String name;
    Thread thread;

    public Worker(String name, Runnable runnable) {
        this.name = name;
        this.firstTask = runnable;
    }

    public void run() {
        thread = new Thread(() -> {
            if (firstTask != null) {
                firstTask.run();
            }
            while (true) {
                try {
                    Thread.sleep(2 * 1000);
                    System.out.println(name + "线程从任务队列中获取任务");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, name);
        thread.start();
    }
}
 
