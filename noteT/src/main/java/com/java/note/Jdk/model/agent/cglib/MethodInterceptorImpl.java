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


    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        System.out.println("CGlib 开始");
//        System.out.println("obj:" + obj);
        System.out.println("obj:" + obj.getClass());
        System.out.println(method.getName());
        System.out.println("method:" + method);
        System.out.println(args[0]);
        System.out.println("MethodProxy:" + proxy);
        System.out.println("MethodProxy:" + proxy.getClass());
        System.out.println("before calling method:" + method.getName());
        Object result = proxy.invokeSuper(obj, args);
        System.out.println("after calling method:" + method.getName());
        System.out.println("CGlib 结束");
        return result;
    }
}
