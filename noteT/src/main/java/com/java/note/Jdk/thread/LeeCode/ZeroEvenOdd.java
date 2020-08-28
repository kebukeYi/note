package com.java.note.Jdk.thread.LeeCode;

import java.util.function.IntConsumer;

/**
 * @Author : mmy
 * @Creat Time : 2020/4/19  23:30
 * @Description
 */
public class ZeroEvenOdd {

    private int n;
    private int index = 1;
    private boolean flagZero = true;
    private boolean flagOdd = false;
    private boolean flagEven = false;
    private Object lock = new Object();

    public ZeroEvenOdd(int n) {
        this.n = n;
    }

    // printNumber.accept(x) outputs "x", where x is an integer.
    public void zero(IntConsumer printNumber) throws InterruptedException {
        synchronized (lock) {
            while (!flagZero) {
                lock.wait();
            }
            printNumber.accept(0);
            flagZero = false;
            if (index % 2 == 1) {
                flagEven = true;
            } else {
                flagOdd = true;
            }
            lock.notifyAll();
        }
    }

    public void even(IntConsumer printNumber) throws InterruptedException {
        for (; index <= 2 * n; ) {
            synchronized (lock) {
                while (!flagEven) {
                    lock.wait();
                }
                printNumber.accept(index);
                index++;
                flagZero = true;
                flagEven = false;
                lock.notifyAll();
            }
        }

    }

    public void odd(IntConsumer printNumber) throws InterruptedException {
        for (; index <= 2 * n; ) {
            synchronized (lock) {
                while (!flagOdd) {
                    lock.wait();
                }
                printNumber.accept(index);
                index++;
                flagZero = true;
                flagOdd = false;
                lock.notifyAll();
            }
        }
    }


}
