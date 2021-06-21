package com.java.note.Jdk.thread.LeeCode;

import java.util.concurrent.Semaphore;
import java.util.function.IntConsumer;

/**
 * @Author : mmy
 * @Creat Time : 2020/4/20  8:30
 * @Description
 */
public class ZeroEventOddTwo {

    private int n;
    private Semaphore zero = new Semaphore(1);
    private Semaphore even = new Semaphore(0);
    private Semaphore odd = new Semaphore(0);

    public ZeroEventOddTwo(int n) {
        this.n = n;
    }

    // printNumber.accept(x) outputs "x", where x is an integer.
    public void zero(IntConsumer printNumber) throws InterruptedException {
        Thread thread=new Thread();
        for (int i = 1; i <= n; i++) {
            System.out.println("zero: " + zero.availablePermits());
            zero.acquire();
            printNumber.accept(0);
            System.out.println();
            if (i % 2 == 1) {
                odd.release();
            } else {
                even.release();
            }
        }
    }

    public void even(IntConsumer printNumber) throws InterruptedException {
        for (int i = 2; i <= n; i += 2) {
            System.out.println("even:" + even.availablePermits());
            even.acquire();
            printNumber.accept(i);
            System.out.println();
            zero.release();
        }
    }

    public void odd(IntConsumer printNumber) throws InterruptedException {
        for (int i = 1; i <= n; i += 2) {
            System.out.println("odd:" + odd.availablePermits());
            odd.acquire();
            printNumber.accept(i);
            System.out.println();
            zero.release();
        }
    }

    public static void main(Strings[] args) {
        ZeroEventOddTwo zeroEvenOdd = new ZeroEventOddTwo(6);
        new Thread(() -> {
            try {
                zeroEvenOdd.zero(System.out::print);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try {
                zeroEvenOdd.even(System.out::print);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try {
                zeroEvenOdd.odd(System.out::print);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

}
