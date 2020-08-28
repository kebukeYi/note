package com.java.note.Jdk.model.single;

/**
 * @Author : mmy
 * @Creat Time : 2020/7/14  16:36
 * @Description 饿汉式
 * 构造器私有化
 * 直接创建对象 static 修饰
 * 对外暴露
 * 使用final修饰
 */
public class Singleton1 {

    public static final Singleton1 SINGLETON1 = new Singleton1();

    private Singleton1() {
    }


}
