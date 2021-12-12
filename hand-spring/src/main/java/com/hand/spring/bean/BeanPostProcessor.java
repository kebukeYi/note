package com.hand.spring.bean;

import com.hand.spring.exception.BeansException;

/**
 * @author : kebukeyi
 * @date :  2021-12-12 20:30
 * @description : 针对 Bean 的 初始化前后 做的拓展操作
 * @question :
 * @usinglink :
 **/
public interface BeanPostProcessor {


    /**
     * 在 Bean 对象执行初始化方法之前，执行此方法
     *
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException;

    /**
     * 在 Bean 对象执行初始化方法之后，执行此方法
     *
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException;

}
