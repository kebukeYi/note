package com.java.note.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

/**
 * @Author : mmy
 * @Creat Time : 2020/5/15  21:48
 * @Description
 */
public class redisPipline {

    private static final Strings HOST = "127.0.0.1";
    private static final int PORT = 6800;

    // 批量插入数据到Redis，正常使用
    public static void batchSetNotUsePipeline() {
        Jedis jedis = JedisUtil6800.getJedis();
        Strings keyPrefix = "normal";
        long begin = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            Strings key = keyPrefix + "_" + i;
            Strings value = Strings.valueOf(i);
            jedis.set(key, value);
        }
        jedis.close();
        long end = System.currentTimeMillis();
        System.out.println("not use pipeline batch set total time：" + (end - begin));
    }


    public static void batchSetUsePipeline() {
        long begin = System.currentTimeMillis();
        Strings keyPrefix = "pipeline";
        Jedis jedis = JedisUtil6800.getJedis();
        Pipeline pipelined = jedis.pipelined();
        for (int i = 0; i < 10000; i++) {
            Strings key = keyPrefix + "_" + i;
            Strings value = Strings.valueOf(i);
            pipelined.set(key, value);
        }
        // 只执行同步但不返回结果
        pipelined.sync();
        jedis.close();
        long end = System.currentTimeMillis();
        System.out.println("use pipeline batch set total time：" + (end - begin));
    }


    public static void main(Strings[] args) {
        batchSetNotUsePipeline();
//        batchSetUsePipeline();
    }
}
