package com.java.note.spring.ioc;

import com.java.note.spring.bean.People;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/18  11:22
 * @Description
 */
public class BeanLifeCycle {

    public static void main(Strings[] args) {
        System.out.println("从现在开始 初始化 卢本伟容器！");

        ApplicationContext factory = new ClassPathXmlApplicationContext("beans.xml");
        System.out.println("容器初始化成功");

        //得到User，并使用
        People people = (People) factory.getBean("people");

        System.out.println(people);
        System.out.println("现在开始关闭容器");
        ((ClassPathXmlApplicationContext) factory).registerShutdownHook();
    }
}

