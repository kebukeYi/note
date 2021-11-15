package com.java.note.Jdk.model.agent.cglib;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @Author : mmy
 * @Creat Time : 2020/9/1  下午 5:38
 * @Description
 */
public class MethodInterceptorImpl implements MethodInterceptor {


    //CGLIB是通过字节码增强处理框架 ASM，来生成字节码并装载到JVM,子类本质上是一个Class 对象,在字节码的层面生成一个子类去集成需要增强的类，
    // 在子类中加入需要增强的方法，让这个子类代替原有的类，完成增强的操作
    //1.生成指定类的Class对象字节数组
    //2.将Class对象字节数组转换为Class对象
    //3.通过 Class.forName 方法将Class对象装载到JVM
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        System.out.println("CGlib 开始");
        //  System.out.println("obj:" + obj);
        System.out.println("obj:" + obj.getClass());
        System.out.println(method.getName());
        System.out.println("method:" + method);
        if ("toString".equals(method.getName())) {
            return null;
        }
        System.out.println(args[0]);
        System.out.println("MethodProxy:" + proxy);
        System.out.println("MethodProxy.getClass() :" + proxy.getClass());
        System.out.println("before calling method:" + method.getName());
        Object result = proxy.invokeSuper(obj, args);
        System.out.println("after calling method:" + method.getName());
        System.out.println("CGlib 结束");
        return result;
    }
}
