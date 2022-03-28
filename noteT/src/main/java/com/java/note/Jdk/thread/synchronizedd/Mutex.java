package com.java.note.Jdk.thread.synchronizedd;

/**
 * @ClassName Mutex
 * @Author kebukeyi
 * @Date 2022/3/27 22:01
 * @Description 最简单的显示锁(不可重入性 、 任意线程 unLock () )
 * @Version 1.0.0
 */
public final class Mutex {

    private boolean isLock = false;
    private Integer count = 0;

    public synchronized void lock() {
        //当一个线程连续调用lock时 将会自己把自己锁住 只能等待其他人 调用多次unLock()方法
        while (isLock) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        count++;
        isLock = true;
    }

    public synchronized void unLock() {
        count--;
        isLock = false;
        notifyAll();
    }

    public Integer getCount() {
        return count;
    }

    public static void main(String[] args) {
        Mutex mutex = new Mutex();
        mutex.lock();
        mutex.unLock();
    }
}
