package com.java.note.Jdk.thread.synchronizedd;

/**
 * @ClassName MutexImproved
 * @Author kebukeyi
 * @Date 2022/3/27 22:11
 * @Description 改良后的 Mutex 支持可重入性、不能任意 unlock()
 * @Version 1.0.0
 */
public final class MutexImproved {

    //当前锁的持有线程
    private Thread owner = null;
    //当前锁的持有次数
    private Long count = 0L;


    public synchronized void lock() {
        Thread current = Thread.currentThread();
        while (count > 0 && owner != current) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        count++;
        owner = current;
    }

    public synchronized void unLock() {
        Thread current = Thread.currentThread();
        if (count > 0 && owner == current) {
            count--;
            if (count == 0) {
                owner = null;
                notifyAll();
            }
        }
    }
}
