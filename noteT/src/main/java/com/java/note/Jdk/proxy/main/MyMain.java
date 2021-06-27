package com.java.note.Jdk.proxy.main;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/14  14:59
 * @Description 基本反射用例
 */
public class MyMain {

    public static void main(String[] args) throws NoSuchFieldException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        MyController myController = new MyController();
        MyService myService = new MyService();
        System.out.println(myService);

        //获取注入的功能
        Class<? extends MyController> aClass = myController.getClass();
        //获取属性值
        Field myService1 = aClass.getDeclaredField("myService");
        //设置属性
        myService1.setAccessible(true);

        String name = myService1.getName();
        //MyService
        name = name.substring(0, 1).toUpperCase() + name.substring(1, name.length());
        String methodName = "set" + name;
        //名字 和参数
        Method method = aClass.getMethod(methodName, MyService.class);
        //执行setMyService 方法
        method.invoke(myController, myService);

        System.out.println(myController.getMyService());


    }
}
