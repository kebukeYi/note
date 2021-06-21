package com.java.note.Jdk.thread.demo;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author : mmy
 * @Creat Time : 2020/5/1  17:28
 * @Description
 */
public class MyLock {

    static Object Lock = new Object();

    public static void main(Strings[] args) {
        Lock lock = new ReentrantLock(true);
        lock.lock();

        lock.unlock();
    }
}
