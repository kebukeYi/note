package com.java.note.Jdk.model.agent.cglib;

import com.java.note.Jdk.model.agent.Operator;
import org.springframework.cglib.core.DebuggingClassWriter;
import org.springframework.cglib.proxy.Enhancer;

/**
 * @Author : mmy
 * @Creat Time : 2020/9/1  下午 5:39
 * @Description
 */
public class ProxyTest {

    public static void main(String[] args) {
        //该设置用于输出jdk动态代理产生的类
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
        // 指定 CGLIB 将动态生成的代理类保存至指定的磁盘路径下
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "D:\\cglib\\final");
        //手动创建目标对象
        Operator operator = new Operator();
        System.out.println("operator:" + operator);
        //手动创建方法拦截器
        MethodInterceptorImpl methodInterceptorImpl = new MethodInterceptorImpl();
        //初始化加强器对象
        Enhancer enhancer = new Enhancer();
        //设置代理类
        enhancer.setSuperclass(operator.getClass());
        //设置代理回调
        enhancer.setCallback(methodInterceptorImpl);
        //创建代理对象
        Operator operationProxy = (Operator) enhancer.create();
        //和 intercept 方法的入参
        System.out.println("operationProxy:" + operationProxy.getClass());
        //调用加强后的操作方法 Object obj 相同地址
        operationProxy.doSomething("deal with");
        // System.out.println(operationProxy.toString());
    }
}
