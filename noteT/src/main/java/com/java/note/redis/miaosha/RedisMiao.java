package com.java.note.redis.miaosha;

import com.java.note.redis.JedisUtil6800;
import redis.clients.jedis.Jedis;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author : kebukeYi
 * @date :  2021-12-14 21:48
 * @description: redis 秒杀扣减库存
 * @question:
 * @link:
 **/
public class RedisMiao {

    private AtomicBoolean isRedis = new AtomicBoolean(true);

    final Jedis jedis = JedisUtil6800.getJedis();
    ReentrantLock reentrantLock = new ReentrantLock();

    public Integer subtractionStockByRedis(String shopId) {
        //reentrantLock.lock();
        try {
            if (isRedis.get()) {
                //扣减总库存 -1
                final Long aLong = jedis.decrBy(shopId, 1);
                //库存扣减成功
                if (aLong > 0) {
                    return 1;
                } else {
                    //库存扣减失败 设置没有库存标志
                    isRedis.set(false);
                    return -1;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //reentrantLock.unlock();
        }
        return -1;
    }


}
 
