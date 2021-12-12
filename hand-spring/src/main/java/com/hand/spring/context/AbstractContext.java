package com.hand.spring.context;

import com.hand.spring.bean.BeanPostProcessor;
import com.hand.spring.core.BeanDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : kebukeYi
 * @date :  2021-12-11 21:18
 * @description:
 * @question:
 * @link:
 **/
public abstract class AbstractContext implements BeanFactory {


    //存放 实例化 + 初始化 = 完整 Bean
    public ConcurrentHashMap<String, Object> singletonObjects = new ConcurrentHashMap<>();

    //存放 实例化  + 未初始化 = 提前留个钩子的 Bean ， 为了避免循环依赖属性注入时出现递归
    public ConcurrentHashMap<String, Object> noInitializingObjects = new ConcurrentHashMap<>();

    //存放所有 组件的元信息
    public ConcurrentHashMap<String, BeanDefinition> beanDefinitionsMap = new ConcurrentHashMap<>();

    public List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();


    @Override
    public Object getBean(String beanName) {
        return null;
    }
}
 
