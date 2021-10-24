package com.java.interview.thread.wait_notify;

/**
 * @author : kebukeYi
 * @date :  2021-10-23 12:24
 * @description:
 * @question:
 * @link: https://stackoverflow.com/questions/11494542/a-thread-holding-multiple-lock-goes-into-wait-state-does-it-release-all-holdi
 **/
public class Lock {

    protected static volatile boolean STOP = true;

    public static void main(String[] args) throws InterruptedException {
        MyThread myThread = new MyThread();
        Thread t1 = new Thread(myThread);
        t1.start();
        while (STOP) {
        }
        System.out.println("After while loop");
        /*
         *
         */
        Thread.sleep(1000 * 10);
        /*
         * Main thread should be Blocked.
         */
        System.out.println("now calling Check()-> perhaps i would be blocked. t1 is holding lock on class instance.");
        //Lock.class lock 阻塞住了
        check();
    }

    public static synchronized void check() {
        System.out.println("inside Lock.check()");
        String threadName = Thread.currentThread().getName();
        System.out.println("inside Lock.Check() method : CurrrentThreadName : " + threadName);
    }
}


class MyThread implements Runnable {
    public MyThread() {
    }

    @Override
    public void run() {
        try {
            System.out.println("inside Mythread's run()");
            classLocking();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void classLocking() throws InterruptedException {
        System.out.println("inside Mythread.classLocking()");
        String threadName = Thread.currentThread().getName();
        System.out.println("inside MyThread.classLocking() : CurrrentThreadName : " + threadName);
        /*
         * outer class locking
         */
        synchronized (Lock.class) {
            System.out.println("I got lock on Lock.class definition");
            Lock.STOP = false;
            /*
             * Outer class lock is not released. Lock on MyThread.class instance is released.
             */
            System.out.println("Lock on MyThread.class instance is released.");
            MyThread.class.wait();
        }
    }
}
 
