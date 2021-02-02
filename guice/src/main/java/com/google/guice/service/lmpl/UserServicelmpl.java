package com.google.guice.service.lmpl;

import com.google.guice.service.UserService;

/**
 * @Author : fang.com
 * @CreatTime : 2021-02-02 09:35
 * @Description :
 * @Version :  0.0.1
 */
public class UserServicelmpl implements UserService {
    @Override
    public void process() {
        System.out.println("我需要做一些业务逻辑");
    }
}
