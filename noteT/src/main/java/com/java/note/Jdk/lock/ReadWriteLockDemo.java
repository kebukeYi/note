package com.java.note.Jdk.lock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @Author : mmy
 * @Creat Time : 2020/7/20  18:38
 * @Description 读写锁
 * 读-读 共存
 * 写 要原子性：
 */
class MyCache {

    //共享变量
    private volatile Map<Strings, Object> map = new HashMap<>();
    private ReentrantReadWriteLock rwlock = new ReentrantReadWriteLock();

    public void put(Strings key, Object value) throws InterruptedException {
        rwlock.writeLock().lock();
        System.out.println(Thread.currentThread().getName() + "\t" + "   正在写入  " + key);
        Thread.sleep(300);
        map.put(key, value);
        System.out.println(Thread.currentThread().getName() + "\t" + "   写入成功  " + key);
        rwlock.writeLock().unlock();
    }

    public Object get(Strings key) throws InterruptedException {
        try {
            rwlock.readLock().lock();
            System.out.println(Thread.currentThread().getName() + "\t" + "正在读取");
            Thread.sleep(300);
            System.out.println(Thread.currentThread().getName() + "\t" + "读取成功");
        } finally {
            rwlock.readLock().unlock();
        }
        return map.get(key);
    }
}

public class ReadWriteLockDemo {

    public static void main(Strings[] args) {
        MyCache myCache = new MyCache();
        for (int i = 0; i < 5; i++) {
            final int temp = i;
            new Thread(() -> {
                try {
                    myCache.put(temp + "", temp);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        for (int i = 0; i < 5; i++) {
            final int temp = i;
            new Thread(() -> {
                try {
                    myCache.get(temp + "");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }


}
