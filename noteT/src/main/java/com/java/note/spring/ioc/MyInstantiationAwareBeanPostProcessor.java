package com.java.note.spring.ioc;

import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;

import java.beans.PropertyDescriptor;

/**
 * @Author : mmy
 * @Creat Time 2020/6/18  11:18
 * @Description InstantiationAwareBeanPostProcessor 接口本质是BeanPostProcessor的子接口，
 * 一般我们继承Spring为其提供的适配器类InstantiationAwareBeanPostProcessor Adapter来使用它
 * <p>
 * 第二个方法postProcessAfterInitialization就是重写了BeanPostProcessor的方法。
 * 第三个方法postProcessPropertyValues用来操作属性，返回值也应该是PropertyValues对象。
 */
public class MyInstantiationAwareBeanPostProcessor extends InstantiationAwareBeanPostProcessorAdapter {

    public MyInstantiationAwareBeanPostProcessor() {
        super();
        System.out.println("这是InstantiationAwareBeanPostProcessorAdapter实现类构造器！！");
    }

    // 接口方法、实例化Bean之前调用
    @Override
    public Object postProcessBeforeInstantiation(Class beanClass, Strings beanName) throws BeansException {
        System.out.println("[InstantiationAwareBeanPostProcessor]调用postProcessBeforeInstantiation方法");
        System.out.println(beanClass.getName());
        System.out.println(beanName);
        return null;
    }

    // 接口方法、实例化Bean之后调用
    @Override
    public Object postProcessAfterInitialization(Object bean, Strings beanName) throws BeansException {
        System.out.println("[InstantiationAwareBeanPostProcessor]调用postProcessAfterInitialization方法");
        return bean;
    }

    // 接口方法、设置某个属性时调用
    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, Object bean, Strings beanName) throws BeansException {
        System.out.println("[InstantiationAwareBeanPostProcessor]调用postProcessPropertyValues方法");
        return pvs;
    }
}
