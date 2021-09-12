package com.java.note.Jdk.proxy.java;

import com.java.note.redis.bean.User;

import java.lang.reflect.Proxy;
import java.util.ArrayList;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/11  15:34
 * @Description
 */
public class JdkClient {


    public static void main(String[] args) {

        assert new ArrayList<String>().getClass() == new ArrayList<Integer>().getClass();
        User user = new User("111", "2222");
        UserProxyServiceImpl us = new UserProxyServiceImpl();
        UserProxyServiceInterceptor usi = new UserProxyServiceInterceptor(us);

        final ClassLoader classLoader = us.getClass().getClassLoader();
        System.out.println(classLoader);
        final ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        System.out.println(contextClassLoader);
        UserProxyService userProxyService = (UserProxyService) Proxy.newProxyInstance(contextClassLoader, us.getClass().getInterfaces(), usi);
        userProxyService.add(user);
        System.out.println("-----------------------------------------------");
        System.out.println(us.hashCode());
        System.out.println(userProxyService.hashCode());
    }
}
