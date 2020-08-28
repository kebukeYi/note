package com.java.note.Jdk.thread.LeeCode;

import java.util.concurrent.Semaphore;
import java.util.function.IntConsumer;

/**
 * @Author : mmy
 * @Creat Time : 2020/4/20  11:14
 * @Description
 */
public class FizzBuzz {

    private int n;
    private int curNum = 1;
    Semaphore flag = new Semaphore(1);

    public FizzBuzz(int n) {
        this.n = n;
    }

    // printFizz.run() outputs "fizz".
    public void fizz(Runnable printFizz) throws InterruptedException {
        while (curNum <= n) {
            flag.acquire(1);
            try {
                if (curNum<=n&&curNum % 3 == 0 && curNum % 5 != 0) {
                    printFizz.run();
                    curNum++;
                }
            } catch (Exception e) {
            } finally {
                flag.release();
            }
        }
    }

    // printBuzz.run() outputs "buzz".
    public void buzz(Runnable printBuzz) throws InterruptedException {
        while (curNum <= n) {
            flag.acquire(1);
            try {
                if (curNum<=n&&curNum % 3 != 0 && curNum % 5 == 0) {
                    printBuzz.run();
                    curNum++;
                }
            } catch (Exception e) {
            } finally {
                flag.release();
            }
        }
    }

    // printFizzBuzz.run() outputs "fizzbuzz".
    public void fizzbuzz(Runnable printFizzBuzz) throws InterruptedException {
        while (curNum <= n) {
            flag.acquire(1);
            try {
                if (curNum<=n&&curNum % 3 == 0 && curNum % 5 == 0) {
                    printFizzBuzz.run();
                    curNum++;
                }
            } catch (Exception e) {
            } finally {
                flag.release();
            }
        }
    }

    // printNumber.accept(x) outputs "x", where x is an integer.
    public void number(IntConsumer printNumber) throws InterruptedException {
        while (curNum <= n) {
            flag.acquire(1);
            try {
                if (curNum<=n&&curNum % 3 != 0 && curNum % 5 != 0) {
                    printNumber.accept(curNum);
                    curNum++;
                }
            } catch (Exception e) {
            } finally {
                flag.release();
            }
        }
    }
}
