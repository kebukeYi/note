package com.dubbo.provider.lmpl;

import com.dubbo.interfaces.DemoService;

/**
 * @Author : mmy
 * @Creat Time : 2020/8/16  10:38
 * @Description
 */
public class IUserServiceImpl implements DemoService {

    public String sayHello(String name) {
        System.out.println("dubbo-comsumer  : " + name);
        return "dubbo-provider : " + name;
    }

}
