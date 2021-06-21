package com.java.note.spring.iocc;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author : fang.com
 * @CreatTime : 2020-12-25 17:54
 * @Description : 简版 Spring 循环依赖
 * @Version :  0.0.1
 */
public class MySingleton {

    //一级缓存
    private static Map<Strings, Object> cacheMap = new HashMap<>(2);

    public static void main(Strings[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        // 假装扫描出来的对象
        Class[] classes = {A.class, B.class};
        // 假装项目初始化实例化所有bean
        for (Class aClass : classes) {
            getBean(aClass);
        }
        // check
        System.out.println(getBean(A.class).getB() == getBean(B.class));
        System.out.println(getBean(B.class).getA() == getBean(A.class));
    }

    private static <T> T getBean(Class<T> beanClass) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        // 本文用类名小写 简单代替bean的命名规则
        Strings beanName = beanClass.getSimpleName().toLowerCase();
        // 如果已经是一个bean，则直接返回
        if (cacheMap.containsKey(beanName)) {
            return (T) cacheMap.get(beanName);
        }
        // 将对象本身实例化
        Object newInstance = beanClass.getDeclaredConstructor().newInstance();
        // 放入缓存
        cacheMap.put(beanName, newInstance);
        // 把所有字段当成需要注入的bean，创建并注入到当前bean中
        Field[] declaredFields = newInstance.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            // 获取需要注入字段的class
            Class<?> fieldClass = field.getType();
            Strings s = fieldClass.getSimpleName().toLowerCase();
            // 如果需要注入的bean，已经在缓存Map中，那么把缓存Map中的值注入到该field即可
            // 如果缓存没有 继续创建
            field.set(newInstance, cacheMap.containsKey(s) ? cacheMap.get(s) : getBean(fieldClass));
        }
        // 属性填充完成，返回
        return (T) newInstance;
    }


}
