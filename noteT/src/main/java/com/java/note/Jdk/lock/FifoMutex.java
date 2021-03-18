package com.java.note.Jdk.lock;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;

/**
 * 先进先出的锁
 *
 * @version v1.0
 */
public class FifoMutex {

    private final AtomicBoolean locked = new AtomicBoolean(false);
    private final Queue<Thread> waiters = new ConcurrentLinkedQueue<Thread>();

    public void lock() {
        boolean wasInterrupted = false;
        Thread current = Thread.currentThread();
        waiters.add(current);
        //只有队首线程可以获取锁
        //是队首元素 直接进去挂起
        while (waiters.peek() == current || !locked.compareAndSet(false, true)) {
            LockSupport.park(this);
            if (Thread.interrupted()) {
                wasInterrupted = true;
            }
            waiters.remove();
            if (wasInterrupted) {
                current.interrupt();
            }
        }
        System.out.println(Thread.currentThread().getName() + "\t" + "获取锁");
    }

    public void lock2() {
        boolean wasInterrupted = false;
        Thread current = Thread.currentThread();
        waiters.add(current);

        // Block while not first in queue or cannot acquire lock
        while (waiters.peek() != current || !locked.compareAndSet(false, true)) {
            LockSupport.park(this);
            if (Thread.interrupted()) // ignore interrupts while waiting
            {
                wasInterrupted = true;
            }
        }
        waiters.remove();
        if (wasInterrupted)          // reassert interrupt status on exit
        {
            current.interrupt();
        }
        System.out.println(Thread.currentThread().getName() + "\t" + "获取锁");
    }


    public void unlock() {
        locked.set(false);
        LockSupport.unpark(waiters.peek());
        System.out.println(Thread.currentThread().getName() + "\t" + "释放锁");
    }

    public static void main(String[] args) {
        final FifoMutex mutex = new FifoMutex();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mutex.lock();
//                    mutex.lock2();
                    Thread.sleep(5000);
                    mutex.unlock();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "AA").start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mutex.lock();
//                    mutex.lock2();
                    mutex.unlock();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "BB").start();
    }

}