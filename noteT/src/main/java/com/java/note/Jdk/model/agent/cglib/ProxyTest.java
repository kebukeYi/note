package com.java.note.Jdk.model.agent.cglib;

import com.java.note.Jdk.model.agent.Operator;
import org.springframework.cglib.proxy.Enhancer;

/**
 * @Author : mmy
 * @Creat Time : 2020/9/1  下午 5:39
 * @Description
 */
public class ProxyTest {
    public static void main(String[] args) {

        Operator operator = new Operator();

        MethodInterceptorImpl methodInterceptorImpl = new MethodInterceptorImpl();

        //初始化加强器对象
        Enhancer enhancer = new Enhancer();
        //设置代理类
        enhancer.setSuperclass(operator.getClass());

        //设置代理回调
        enhancer.setCallback(methodInterceptorImpl);

        //创建代理对象
        Operator operationProxy = (Operator) enhancer.create();

        //调用加强后的操作方法
        operationProxy.doSomething();
    }
}
