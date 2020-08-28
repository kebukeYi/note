package com.java.note.redis.JedisTest;

import redis.clients.jedis.Jedis;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/16  7:58
 * @Description
 */
public class BadConcurrentJedisTest {


    private static final ExecutorService pool = Executors.newFixedThreadPool(20);

    private static final Jedis jedis = new Jedis("127.0.0.1", 6379);

    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            pool.execute(new RedisSet());
        }
    }

    static class RedisSet implements Runnable {
        @Override
        public void run() {
            while (true) {
                jedis.set("hello", "world");
            }
        }

    }
}
