package com.java.note.Jdk.model.agent;

import com.java.note.Jdk.model.agent.Operate;

/**
 * @Author : mmy
 * @Creat Time : 2020/9/1  下午 5:24
 * @Description 静态代理
 */
public class Operator implements Operate {
    @Override
    public void doSomething() {
        System.out.println("I'm Operator for  doing something ");
    }
}
