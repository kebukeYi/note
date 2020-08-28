package com.java.note.redis;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import java.util.Collections;
import java.util.UUID;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/3  15:19
 * @Description 分布式锁
 */
@RestController
@RequestMapping("/redis")
public class RedisLockController {

    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";
    private static final String LOCK_KEY = "lockKey";
    private static final String STOCK = "stock";


    static Object lock = new Object();

    @Autowired
    private Redisson redisson;

    /**
     * 服务器A
     * 第一种解法
     */
    @GetMapping("/stock")
    public String redisLock() {
        Jedis jedis = JedisUtil6800.getJedis();
        String clinenId = UUID.randomUUID().toString();
        Object eval;
        try {
            String result = jedis.set(LOCK_KEY, clinenId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, 10);
            if (!LOCK_SUCCESS.equals(result)) {
                return "error";
            }

            int stock = Integer.parseInt(JedisUtil6800.getString1(STOCK));
            if (stock > 0) {
                stock--;
                JedisUtil6800.setString1(STOCK, stock + "");
                System.out.println("减库存成功,剩余库存：" + stock);
            } else {
                System.out.println("失败");
            }

        } finally {
            /**
             * lua 脚本来解
             * 是否是同一个线程
             *  lua脚本，用来释放分布式锁 - 如果使用的较多，可以封装到文件中, 再进行调用
             * 但是匹配和删除 擦操作并非是原子操作  因此可以采用Lua 脚本解决
             //        if redis.call('get', KEYS[1]) == ARGV[1]
             //            then
             //                return redis.call('del', KEYS[1])
             //        else
             //            return 0
             //        end
             */

            String luaScript = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
            eval = jedis.eval(luaScript, Collections.singletonList(LOCK_KEY), Collections.singletonList(clinenId));

        }
        return eval.toString();
    }


    /**
     * 服务器A
     * 第二种解法
     */
    @GetMapping("/redisson")
    public String redisson() {
        RLock rLock = redisson.getLock(LOCK_KEY);
        try {
            //加锁 -> 默认设置一个key 超时30秒
            rLock.lock();
            int stock = Integer.parseInt(JedisUtil6800.getString1(STOCK));
            if (stock > 0) {
                stock--;
                JedisUtil6800.setString1(STOCK, stock + "");
                System.out.println("减库存成功,剩余库存：" + stock);
            } else {
                System.out.println("失败");
            }
        } finally {
            rLock.unlock();
        }
        return "OK";
    }
}
