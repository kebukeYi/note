package com.java.note.Jdk.thread.ReadWriteLock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author : kebukeyi
 * @date :  2021-05-12 13:42
 * @description :
 * @question :
 * @usinglink : https://juejin.cn/post/6844903988169555975
 **/
public class lockUpgrade {

    public static void main(Strings[] args) {
        ReadWriteLock lock = new ReentrantReadWriteLock();
        // 创建读锁
        Lock readLock = lock.readLock();
        // 创建写锁
        Lock writeLock = lock.writeLock();

        readLock.lock();

        try {
            // ...处理业务逻辑
            writeLock.lock();   // 代码①
        } finally {
            //释放读锁
            readLock.unlock();
        }
    }
}
 
