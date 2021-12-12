package com.hand.spring.BeanPostProcessor;

import com.hand.spring.annoation.Component;
import com.hand.spring.bean.BeanPostProcessor;
import com.hand.spring.exception.BeansException;
import com.hand.spring.service.IUserService;
import com.hand.spring.service.UserService;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author : kebukeYi
 * @date :  2021-12-12 20:35
 * @description:
 * @question:
 * @link:
 **/
@Component
public class HandSpringBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if ("userService".equals(beanName)) {
            ((UserService) bean).setBeanPostProcessorName("HandSpringBeanPostProcessor");
            System.out.println("初始化之前");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        //创建代理类
        //有没有必要进行AOP 需要 一 一 匹配
        if ("userService".equals(beanName)) {
            //开启AOP之后  就去找已经定义的切点 然后层层包装
            Object proxyInstance = Proxy.newProxyInstance(HandSpringBeanPostProcessor.class.getClassLoader(), bean.getClass().getInterfaces(), new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    System.out.println("开始代理逻辑");
                    final Object invoke = method.invoke(bean, args);
                    System.out.println(invoke);
                    System.out.println("结束代理逻辑");
                    return invoke;
                }
            });
            return proxyInstance;
        }
        return bean;
    }
}
 
