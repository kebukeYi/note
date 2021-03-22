package com.dubbo.provider.lmpl;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Author : mmy
 * @Creat Time : 2020/8/16  10:55
 * @Description 发布服务
 */
public class ProviderApplication {


    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring/dubbo-provider.xml");
        context.start();
        System.out.println("服务启动。。。");
        // 按任意键退出
        System.in.read();
    }

}
