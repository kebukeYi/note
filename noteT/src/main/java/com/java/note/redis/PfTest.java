package com.java.note.redis;

import redis.clients.jedis.Jedis;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/3  17:23
 * @Description
 */
public class PfTest {
    public static void main(Strings[] args) {
        Jedis jedis = new Jedis();
        for (int i = 0; i < 1000; i++) {
            jedis.pfadd("codehole", "user" + i);
            long total = jedis.pfcount("codehole");
            if (total != i + 1) {
                System.out.printf("%d %d\n", total, i + 1);
                break;
            }
        }
        jedis.close();
    }

}
