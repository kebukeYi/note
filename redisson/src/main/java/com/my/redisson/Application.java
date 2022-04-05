package com.my.redisson;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName Application
 * @Author kebukeyi
 * @Date 2022/4/3 10:50
 * @Description Redis的环境采用三主三从的方式搭建一套Redis-Cluster集群环境
 * @Version 1.0.0
 */
public class Application {

    public static void redisson() throws InterruptedException {
        Config config = new Config();
        config.useClusterServers()
                .addNodeAddress("redis://192.168.43.156:7001")
                .addNodeAddress("redis://192.168.43.156:7002")
                .addNodeAddress("redis://192.168.43.156:7003")
                .addNodeAddress("redis://192.168.43.156:7004")
                .addNodeAddress("redis://192.168.43.156:7005")
                .addNodeAddress("redis://192.168.43.156:7006");
        RedissonClient redissonClient = Redisson.create();
        testRedissonSimpleLock(redissonClient);
    }

    public static void testRedissonSimpleLock(RedissonClient redissonClient) throws InterruptedException {
        RLock lock = redissonClient.getLock("lock");
        //进入一个while的死循环中、间歇性的一直获取锁，相当于就阻塞住了
        lock.lock();
        //要指定个时间，在这个时间内获取锁都失败的话就直接退出、保证不阻塞
        lock.lockInterruptibly(30, TimeUnit.SECONDS);
        //加锁时，设置 等待获取锁时间30s、锁租用时间10s,之后会被自动释放
        lock.tryLock(30, 10, TimeUnit.SECONDS);
        lock.unlock();
        lock.forceUnlock();
        lock.forceUnlockAsync();
    }
}
