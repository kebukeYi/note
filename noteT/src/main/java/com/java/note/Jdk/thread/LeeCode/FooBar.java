package com.java.note.Jdk.thread.LeeCode;

/**
 * @Author : mmy
 * @Creat Time : 2020/4/19  18:48
 * @Description
 */
public class FooBar {

    private int n;
    Object lock = new Object();
    private int flag = 1;

    public FooBar(int n) {
        this.n = n;
    }

    public void foo(Runnable printFoo) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            synchronized (lock) {
                while (flag != 1) {
                    lock.wait();
                    //释放占有的对象锁，线程进入等待池，释放cpu,而其他正在等待的线程即可抢占此锁，获得锁的线程即可运行程序。而sleep()不同的是，线
                    // 程调用此方法后，会休眠一段时间，休眠期间，会暂时释放cpu，但并不释放对象锁。也就是说，在休眠期间，其他线程依然无法进入此代码内部。
                    // 休眠结束，线程重新获得cpu,执行代码。
                    // wait()和sleep()最大的不同在于wait()会释放对象锁，而sleep()不会！
                }
                // printFoo.run() outputs "foo". Do not change or remove this line.
                printFoo.run();
                flag = 2;
//             lock.notifyAll(); //是唤醒所有等待的线程。
                lock.notify();
                /*
                该方法会唤醒因为调用对象的wait()而等待的线程，其实就是对对象锁的唤醒，从而使得wait()的线程可以有机会获取对象锁。
                调用notify()后，并不会立即释放锁，而是继续执行当前代码，直到synchronized中的代码全部执行完毕，才会释放对象锁。
                JVM则会在等待的线程中调度一个线程去获得对象锁，执行代码。
                需要注意的是，wait()和notify()必须在synchronized代码块中调用。
                 */
            }
        }
    }

    public void bar(Runnable printBar) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            synchronized (lock) {
                while (flag != 2) {
                    lock.wait();
                }
                // printFoo.run() outputs "foo". Do not change or remove this line.
                printBar.run();
                flag = 1;
                lock.notifyAll();
            }
        }
    }
}
