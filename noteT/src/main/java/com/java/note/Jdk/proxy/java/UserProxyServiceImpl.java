package com.java.note.Jdk.proxy.java;

import com.java.note.redis.bean.User;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/11  15:18
 * @Description Jdk 实现动态代理 必须 要实现一个接口；而Cglib  就不用；
 */
public class UserProxyServiceImpl implements UserProxyService {

    /**
     * 需求：在插入库时对 字段 进行无侵入 校验
     */
    @Override
    public void add(User user) {
        System.out.println(" UserProxyServiceImpl 添加用户成功");
    }

}
