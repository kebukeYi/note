package com.java.note.Jdk.proxy.java;

import com.java.note.redis.bean.User;

import java.lang.reflect.Proxy;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/11  15:34
 * @Description
 */
public class JdkClient {

    public static void main(Strings[] args) {
        User user = new User("111", "2");
        UserProxyServiceImpl us = new UserProxyServiceImpl();
        UserProxyServiceInterceptor usi = new UserProxyServiceInterceptor(us);
        UserProxyService userProxyService = (UserProxyService) Proxy.newProxyInstance(us.getClass().getClassLoader(), us.getClass().getInterfaces(), usi);
        userProxyService.add(user);
        System.out.println("-----------------------------------------------");
        System.out.println(us.hashCode());
        System.out.println(userProxyService.hashCode());
    }
}
