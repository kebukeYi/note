package com.java.note.Jdk.proxy.cglib;

import com.java.note.redis.bean.User;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/11  15:50
 * @Description
 */
public class UserCglibServiceImpl {

    public void add(User user) {
        System.out.println("UserCglibServiceImpl 添加用户成功");
    }
}
