package com.java.note.Jdk.model.agent.mutal;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author : kebukeyi
 * @date :  2021-04-23 18:09
 * @description :
 * @usinglink :
 **/
public class JavaDynamicProxy implements InvocationHandler {

    private Object object;

    public JavaDynamicProxy(Object object) {
        this.object = object;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("多重代理之开始：" + System.currentTimeMillis());
        Object result = method.invoke(object, args);
        System.out.println("多重代理之结束：" + System.currentTimeMillis());
        return result;
    }
}
 
