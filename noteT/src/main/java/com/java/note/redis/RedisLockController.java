package com.java.note.redis;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/3  15:19
 * @Description 分布式锁
 */
//@RestController
//@RequestMapping("/redis")
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
     * 服务器A redis 分布式锁
     * 第一种解法
     */
    // @GetMapping("/stock")
    public String redisLock() {
        Jedis jedis = JedisUtil6800.getJedis();
        String clinenId = UUID.randomUUID().toString();
        Object eval;
        try {
            //设置锁
            String result = jedis.set(LOCK_KEY, clinenId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, 10);
            if (!LOCK_SUCCESS.equals(result)) {
                return "error";
            }
            //加锁成功 开始扣库存
            //先获取到 key
            int stock = Integer.parseInt(JedisUtil6800.getString1(STOCK));
            if (stock > 0) {
                stock--;
                //再重新设置回去
                JedisUtil6800.setString1(STOCK, stock + "");
                System.out.println("减库存成功,剩余库存：" + stock);
            } else {
                System.out.println("扣减库存失败");
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

    //并发测试 redis 之 jedis 是否线程安全
    public static void testAccessRedis() throws InterruptedException {
        Jedis jedis = JedisUtil6800.getJedis();
        jedis.select(2);
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final ConcurrentHashMap<String, Object> concurrentHashMap = new ConcurrentHashMap();
        for (int i = 0; i < 50; i++) {
            new Thread(() -> {
                try {
                    System.out.println("开始阻塞");
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("开始执行redis");
                //并发设置 redis 正确
                final long incr = jedis.incr("key");
                //肯定不是安全的
                //final boolean incr = redisUtil.set("key", Thread.currentThread().getName());
                concurrentHashMap.put(Thread.currentThread().getName(), incr);
            }, "thread-" + i).start();
        }
        countDownLatch.countDown();
        System.out.println("放行");
        Thread.sleep(10000);
        System.out.println(concurrentHashMap);
    }


    /**
     * 服务器A
     * 第二种解法 Redisson 分布式锁
     */
    // @GetMapping("/redisson")
    public static String redisson2() {
        Redisson redisson = redisson();
        RLock rLock = redisson.getLock(LOCK_KEY);
        try {
            //加锁 -> 默认设置一个 key 超时30秒
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

    public static Redisson redisson() {
        //单机模式
        Config config = new Config();
        config.useSingleServer().setAddress("redis://localhost:6379").setDatabase(1);
        return (Redisson) Redisson.create(config);
    }

    public static void main(String[] args) throws InterruptedException {
        // redisson();
        testAccessRedis();
    }
}
