package com.hand.spring.context;

import com.hand.spring.core.BeanDefinition;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : kebukeYi
 * @date :  2021-12-11 21:18
 * @description:
 * @question:
 * @link:
 **/
public abstract class AbstractContext implements BeanFactory {


    public ConcurrentHashMap<String, Object> singletonObjects = new ConcurrentHashMap<>();

    public ConcurrentHashMap<String, Object> noInitializingObjects = new ConcurrentHashMap<>();

    public ConcurrentHashMap<String, BeanDefinition> beanDefinitionsMap = new ConcurrentHashMap<>();


    @Override
    public Object getBean(String beanName) {
        return null;
    }
}
 
