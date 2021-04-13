package com.java.note.redis.config;

import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/3  16:35
 * @Description
 */
@Configuration
public class RedissonConfig {


//    @Bean
//    public Redisson redisson() {
//        //单机模式
//        Config config = new Config();
//        config.useSingleServer().setAddress("redis://localhost:6379").setDatabase(1);
//        return (Redisson) Redisson.create(config);
//    }


}
