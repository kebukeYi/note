package com.java.note.Jdk.threadpool.two;

/**
 * @author : kebukeYi
 * @date :  2022-01-02 00:37
 * @description: 回顾 ThreadPoolExecutor 类的 addWorker()、new Worker()、worker.thread.start() 方法，提出了如下疑问：
 * @question: 一种是Jdk原生Work类实现了Runnable接口，另外一种是自己想的Worker类并且没有实现Runnable接口，那么这两种实现有什么不一样的吗?
 * @link:
 **/
public class ThreadPoolDemo {

    public static void main(String[] args) throws InterruptedException {
        //新建线程 && 并且保存用户任务
        final Work work = new Work("one", () -> {
            System.out.println("A用户任务被当前线程 " + Thread.currentThread().getName() + " 执行 ");
        });
        //执行用户任务 && 获取任务队列中的任务
        work.thread.start();
        //设置线程 one 中断一下 -> 会被捕获异常 -> 执行退出函数
        //work.thread.interrupt();

        System.out.println("====================================");

        //保存用户任务
        final Worker worker = new Worker("two", () -> {
            System.out.println("B用户任务被当前线程 " + Thread.currentThread().getName() + " 执行 ");
        });
        //新建线程  && 并且执行用户任务 && 获取任务队列中的任务
        worker.run();
        //设置线程 two 中断一下 -> 会被捕获异常 -> 执行退出函数
        //worker.thread.interrupt();

        //避免主线程退出
        Thread.sleep(3 * 1000);
    }
}

//原生Jdk的实现 实现了 Runnable 接口
class Work implements Runnable {
    Runnable firstTask;
    String name;
    Thread thread;
    volatile long completedTasks;

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
        //突然退出   发生异常 当前线程 需要做一些处理
        boolean completedAbruptly = false;
        Runnable task = firstTask;
        try {
            while (true) {
                Throwable thrown = null;
                try {
                    try {
                        try {
                            //用户传入的第一个任务
                            if (task != null) {
                                task.run();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            throw e;
                        }
                        Thread.sleep(2 * 1000);
                        //模拟线程从任务队列中循环获取任务
                        System.out.println(name + "线程从任务队列中获取任务");
                    } catch (Exception e) {
                        thrown = e;
                        throw e;
                    } finally {
                        afterExecute(task, thrown);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    completedAbruptly = true;
                    break;
                } finally {
                    task = null;
                    completedTasks++;
                }
            }
        } finally {
            processWorkerExit(completedAbruptly);
        }

    }

    private void afterExecute(Runnable firstTask, Throwable thrown) {

    }

    public void processWorkerExit(boolean completedAbruptly) {
        if (completedAbruptly) {
            System.out.println("处理异常退出的线程：" + Thread.currentThread().getName());
            return;
        }
    }
}

//自定义实现 并且 没有实现 Runnable 接口
class Worker {
    Runnable firstTask;
    String name;
    Thread thread;
    volatile long completedTasks;

    public Worker(String name, Runnable runnable) {
        this.name = name;
        this.firstTask = runnable;
    }

    public void run() {
        //突然退出   发生异常 当前线程 需要做一些处理
        thread = new Thread(() -> {
            Runnable task = firstTask;
            boolean completedAbruptly = false;
            try {
                while (true) {
                    Throwable thrown = null;
                    try {
                        try {
                            try {
                                //用户传入的第一个任务
                                if (task != null) {
                                    task.run();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                throw e;
                            }
                            Thread.sleep(2 * 1000);
                            //模拟线程从任务队列中循环获取任务
                            System.out.println(name + "线程从任务队列中获取任务");
                        } catch (Exception e) {
                            thrown = e;
                            throw e;
                        } finally {
                            afterExecute(task, thrown);
                        }
                    } catch (Exception e) {
                        completedAbruptly = true;
                        break;
                    } finally {
                        task = null;
                        completedTasks++;
                    }
                }
            } finally {
                processWorkerExit(completedAbruptly);
            }
        }, name);

        thread.start();
    }

    private void afterExecute(Runnable firstTask, Throwable thrown) {
    }

    public void processWorkerExit(boolean completedAbruptly) {
        if (completedAbruptly) {
            System.out.println("处理异常退出的线程：" + Thread.currentThread().getName());
            return;
        }
    }
}
 
