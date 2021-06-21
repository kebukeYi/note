package com.java.note.Jdk.thread.LeeCode;

/**
 * @Author : mmy
 * @Creat Time : 2020/4/19  17:12
 * @Description
 */
public class Foo {

    Object lock = new Object();
    boolean finshiOne;
    boolean finshiTwo;

    public Foo() {
    }

    public void first(Runnable printFirst) throws InterruptedException {
        // printFirst.run() outputs "first". Do not change or remove this line.
        synchronized (lock) {
            printFirst.run();
            finshiOne = true;
            lock.notifyAll();
        }

    }

    public void second(Runnable printSecond) throws InterruptedException {
        // printSecond.run() outputs "second". Do not change or remove this line.
        synchronized (lock) {
            while (!finshiOne) {
                lock.wait();
            }
            printSecond.run();
            finshiTwo = true;
            lock.notifyAll();
        }
    }

    public void third(Runnable printThird) throws InterruptedException {
        // printThird.run() outputs "third". Do not change or remove this line.
        synchronized (lock) {
            while (!finshiTwo) {
                lock.wait();
            }
            printThird.run();
        }
    }

    public static void main(Strings[] args) throws InterruptedException {
        Foo foo = new Foo();
        synchronized (foo.lock) {
            foo.first(() -> System.out.println("one"));
            foo.second(() -> System.out.println("two"));
            foo.third(() -> System.out.println("three"));
        }
    }
}
