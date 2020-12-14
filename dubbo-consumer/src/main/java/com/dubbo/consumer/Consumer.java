package com.dubbo.consumer;

import com.dubbo.interfaces.DemoService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Author : mmy
 * @Creat Time : 2020/8/16  11:25
 * @Description
 */
public class Consumer {

    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"classpath:dubbo-comsumer.xml"});

        context.start();
        DemoService demoService = (DemoService) context.getBean("demoService"); // 获取远程服务代理
        String hello = demoService.sayHello("world"); // 执行远程方法
        System.out.println(hello); // 显示调用结果

    }

}