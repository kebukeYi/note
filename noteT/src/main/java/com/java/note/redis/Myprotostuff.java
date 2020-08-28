package com.java.note.redis;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.java.note.redis.bean.User;
import redis.clients.jedis.Jedis;

/**
 * @Author : mmy
 * @Creat Time : 2020/5/16  12:56
 * @Description
 */
public class Myprotostuff {

    private static RuntimeSchema<User> schema = RuntimeSchema.createFrom(User.class);

    public static User getProtostuff(String key) {
        Jedis jedis = JedisUtil6800.getJedis();
        byte[] bytes = jedis.get(key.getBytes());
        jedis.close();
        if (bytes != null) {
            User user = schema.newMessage();
            ProtobufIOUtil.mergeFrom(bytes, user, schema);
            return user;
        }
        return null;
    }

    public static String setSchema(User user, String key) {
        Jedis jedis = JedisUtil6800.getJedis();
        byte[] bytes = ProtobufIOUtil.toByteArray(user, schema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
        String result = jedis.set(key.getBytes(), bytes);
        jedis.close();
        return result;
    }

    public static void main(String[] args) {
        User user = new User("kkk", "2");
        String key = "mmy";
        System.out.println(setSchema(user, key));
        System.out.println(getProtostuff(key));
    }

}
