package com.dubbo.consumer;

import com.dubbo.interfaces.DemoService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Author : mmy
 * @Creat Time : 2020/8/16  11:25
 * @Description
 */
public class Consumer {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"classpath:dubbo-comsumer.xml"});
        context.start();
        // 获取远程服务代理
        DemoService demoService = (DemoService) context.getBean("demoService");
        // 执行远程方法
        String hello = demoService.sayHello("world");
        // 显示调用结果
        System.out.println(hello);

    }

}