package com.java.note.spring.ioc;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/18  11:18
 * @Description BeanPostProcessor接口包括2个方法postProcessAfterInitialization和postProcessBeforeInitialization，
 * 这两个方法的第一个参数都是要处理的Bean对象，第二个参数都是Bean的name。返回值也都是要处理的Bean对象。这里要注意。
 */
@Component
public class MyBeanPostProcessor implements BeanPostProcessor {

    public MyBeanPostProcessor() {
        super();
        System.out.println("这是BeanPostProcessor实现类构造器！！");
        // TODO Auto-generated constructor stub
    }

    /**
     * 初始化之前
     * 不在 before 使用代理 因为 jdk 的代理是面向接口，而 init 和 destroy 方法是 实现类的
     * afterPropertiesSet-3  custom init-method-2   postProcessBeforeInitialization-1
     */
    @Override
    public Object postProcessBeforeInitialization(Object arg0, String beanName) throws BeansException {
        System.out.println("[BeanPostProcessor]接口方法postProcessBeforeInitialization对属性进行更改！" + arg0 + " == beanName " + beanName);
        return arg0;
    }

    /**
     * 在 after 中生成 代理对象，在执行方法前后开启和关闭事务
     * afterPropertiesSet-3  postProcessAfterInitialization-4
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("[BeanPostProcessor]postProcessAfterInitialization！" + bean + " == beanName " + beanName);
        Object proxy = Proxy.newProxyInstance(bean.getClass().getClassLoader(), bean.getClass().getInterfaces(),
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        System.out.println("开始事务");
                        Object result = method.invoke(bean, args);
                        System.out.println("关闭事务");
                        return result;
                    }
                });

        return bean;
    }


}
