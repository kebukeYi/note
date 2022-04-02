package com.java.note.Jdk.thread.specific_storage;

import org.jetbrains.annotations.Contract;

/**
 * @ClassName Log
 * @Author kebukeyi
 * @Date 2022/4/2 12:17
 * @Description
 * @Version 1.0.0
 */
public class Log {

    private static final ThreadLocal threadLocal = new ThreadLocal();

    //默认redis锁的有效时长 秒/单位
    private static final Integer MAX_REDIS_LOCK = 2;
    //默认最大 延期次数   加上 首次 一共 3次
    private static final Integer MAX_REDIS_LOCK_COUNT = 20;
    //当前次数
    private static Integer REDIS_LOCK_COUNT = 0;

    //启动一个等待该线程终止的线程
    public static Thread startWatcher(final TSLog tsLog) {
        //被监听的线程
        Thread target = Thread.currentThread();
        //监听线程
        Thread watcher = new Thread(() -> {
            //默认 失败
            boolean addRedisLockTime = false;
            try {
                System.out.println("startWatcher for " + target.getName() + " begin ");
                //第一次：1. 假如监听线程刚启动，被监听线程完事了，那么 isOK()返回true，取反直接跳出循环
                //             2. 假如监听线程启动，被监听线程没有完事，那么 isOK()返回false， 并且次数允许，默认加锁成功，监听线程最大等待时间为锁的有效期
                //第二次：1. 监听线程醒过来，完事的话就直接退出了，不再执行加锁逻辑；那么当执行加锁时，说明还没完事。就有可能加锁失败
                while ((!tsLog.isOK() && REDIS_LOCK_COUNT <= MAX_REDIS_LOCK_COUNT) || addRedisLockTime) {
                    //基本描述：监听线程加入 被监视线程的 等待|通知 队列中
                    //基本参数：默认等待 redis 分布式锁的有效时长
                    //情况1：假如redis分布式锁快到期了并且还未释放共享互斥资源  那么就要延长占有redis分布式锁的时长 当然不能一直占有 有最大次数
                    target.join(MAX_REDIS_LOCK * 1000);
                    System.out.println("startWatcher for " + target.getName() + " again ");
                    //情况一：时间到了  监听线程自动醒了过来 需要判断是否继续
                    //情况二：监听线程被动醒了过来
                    //              1. 被监听线程完事了 那么就直接退出 不用再延长锁时间
                    //              2. 监听线程被中断唤醒了 那么这里先执行取消监听
                    if (tsLog.isOK()) break;
                    REDIS_LOCK_COUNT++;
                    //这里其实也有可能抛出 超时异常 中断异常 等等
                    addRedisLockTime = addRedisLockTime(MAX_REDIS_LOCK);
                    //这里加锁失败的话 就直接退出了
                    if (!addRedisLockTime) tsLog.watchFail();
                }
            } catch (InterruptedException e) {
                //暂且通知被监听线程 监听失败
                tsLog.watchFail();
                System.out.println("startWatcher for " + target.getName() + " fail " + " REDIS_LOCK_COUNT : " + REDIS_LOCK_COUNT + "  addRedisLockTime :  " + addRedisLockTime);
            }
            System.out.println("startWatcher for " + target.getName() + " end ");
        });
        //开始监听
        watcher.start();
        return watcher;
    }

    @Contract(pure = true)
    public static boolean addRedisLockTime(Integer time) {
        //假如这一步成功
        return true;
    }
}
