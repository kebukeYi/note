package com.java.note.Jdk.thread.Semaphore;

import java.util.concurrent.Semaphore;

/**
 * 数据库链接池
 */
public class Pool {
    /**
     * 可同时访问资源的最大线程数
     */
    private static final int MAX_AVAILABLE = 100;

    /**
     * 信号量 表示：可获取的对象通行证
     */
    private final Semaphore available = new Semaphore(MAX_AVAILABLE, true);

    /**
     * 共享资源，可以想象成 items 数组内存储的都是Connection对象 模拟是链接池
     */
    protected Object[] items = new Object[MAX_AVAILABLE];

    /**
     * 共享资源占用情况，与items数组一一对应，
     * 比如：items[0]对象被外部线程占用，那么 used[0] == true，否则used[0] == false
     */
    protected boolean[] used = new boolean[MAX_AVAILABLE];

    /**
     * 获取一个空闲对象
     * 如果当前池中无空闲对象，则等待..直到有空闲对象为止
     */
    public Object getItem() throws InterruptedException {
        available.acquire();
        return getNextAvailableItem();
    }

    /**
     * 归还对象到池中
     */
    public void putItem(Object x) {
        if (markAsUnused(x))
            available.release();
    }

    /**
     * 获取池内一个空闲对象，获取成功则返回Object，失败返回Null
     * 成功后将对应的 used[i] = true
     */
    private synchronized Object getNextAvailableItem() {
        for (int i = 0; i < MAX_AVAILABLE; ++i) {
            if (!used[i]) {
                used[i] = true;
                return items[i];
            }
        }
        return null;
    }

    /**
     * 归还对象到池中，归还成功返回true
     * 归还失败：
     * 1.池中不存在该对象引用，返回false
     * 2.池中存在该对象引用，但该对象目前状态为空闲状态，也返回false
     */
    private synchronized boolean markAsUnused(Object item) {
        for (int i = 0; i < MAX_AVAILABLE; ++i) {
            if (item == items[i]) {
                if (used[i]) {
                    used[i] = false;
                    return true;
                } else
                    return false;
            }
        }
        return false;
    }

}
