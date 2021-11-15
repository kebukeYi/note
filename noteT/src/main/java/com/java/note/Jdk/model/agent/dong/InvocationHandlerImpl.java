package com.java.note.Jdk.model.agent.dong;

import com.java.note.Jdk.model.agent.Operate;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Author : mmy
 * @Creat Time : 2020/9/1  下午 5:28
 * @Description
 */
public class InvocationHandlerImpl implements InvocationHandler {

    private Operate operate;

    //注入操作者对象
    public InvocationHandlerImpl(Operate operate) {
        this.operate = operate;
    }

    public InvocationHandlerImpl() {
    }


    /***
     * @param proxy 生成的代理对象
     * @param method 代理对象中要执行的方法
     * @param args 方法参数
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println(" InvocationHandlerImpl 动态代理 开始");
        System.out.println("before calling method: " + method.getName());
        //调用操纵者的具体操作方法
        Object result = method.invoke(operate, args);
        // String result = "调用其他接口的返回值";
        System.out.println("result : " + result);
        System.out.println("after calling method: " + method.getName());
        System.out.println("动态代理 结束");

//        return proxy;
        return result;
//        return null;
    }


}
