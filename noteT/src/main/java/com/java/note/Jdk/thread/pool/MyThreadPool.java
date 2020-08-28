package com.java.note.Jdk.thread.pool;

import java.util.List;

/**
 * @Author : mmy
 * @Creat Time : 2020/5/9  22:09
 * @Description
 */
public interface MyThreadPool {

    /**
     * 执行多线程任务
     *
     * @param task
     */
    void excute(Runnable task);

    void excute(Runnable[] tasks);

    void excute(List<Runnable> tasks);

    /**
     * 获取已完成任务数量
     *
     * @return
     */
    int getFinishedTaskNum();

    /**
     * 获取任务数量
     *
     * @return
     */
    int getThreadTaskNum();

    /**
     * 获取线程数量
     *
     * @return
     */
    int getThreadWorkerNum();

    /**
     * 销毁线程池
     */
    void destroy();
}
