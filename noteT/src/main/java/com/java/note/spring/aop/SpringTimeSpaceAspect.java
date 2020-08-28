package com.java.note.spring.aop;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/18  16:21
 * @Description
 */
@Aspect
public class SpringTimeSpaceAspect implements MethodInterceptor {

    private long beforeTime;


    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        return null;
    }
}
