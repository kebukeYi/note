package com.java.note.Jdk.model.agent;

/**
 * @Author : mmy
 * @Creat Time : 2020/9/1  下午 5:24
 * @Description 静态代理
 */
public class Operator implements Operate {

    @Override
    public final String toString() {
        return "Operator 重写的 toString() 方法";
    }

    @Override
    public final String doSomething(String name) {
        System.out.println("I'm Operator for  doing something " + name);
        return "自己方法的返回值：success-4";
    }
}
