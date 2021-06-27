package com.java.note.Jdk.proxy.cglib;

import com.java.note.redis.bean.User;
import org.springframework.cglib.proxy.Enhancer;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/11  15:54
 * @Description
 */
public class CglibClient {

    public static void main(String[] args) {
        User user = new User("1", "2");
        Enhancer enhancer = new Enhancer();
        //设置 动态代理的 父类
        enhancer.setSuperclass(UserCglibServiceImpl.class);
        //设置增强器
        enhancer.setCallback(new UserCglibIntercetor());
        //得到代理对象
        UserCglibServiceImpl usl = (UserCglibServiceImpl) enhancer.create();
        usl.add(user);
    }
}
