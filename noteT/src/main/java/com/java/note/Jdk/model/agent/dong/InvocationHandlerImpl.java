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


    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        System.out.println("before calling method: " + method.getName());
        //调用操纵者的具体操作方法
        method.invoke(operate, objects);
        System.out.println("after calling method: " + method.getName());
        return null;
    }


}
