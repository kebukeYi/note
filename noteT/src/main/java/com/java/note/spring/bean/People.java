package com.java.note.spring.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.util.StringValueResolver;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/8  22:31
 * @Description
 */
@Data
@AllArgsConstructor
@Entity
public class People implements BeanFactoryAware, BeanNameAware, InitializingBean, DisposableBean, EmbeddedValueResolverAware {

    @Id
    private int id;

    private String name;
    private String age;

    @Transient
    private BeanFactory beanFactory;

    private String beanName;

    public People(String name, String age) {
        this.name = name;
        this.age = age;
    }

    public People() {
        System.out.println("【构造器】调用People的构造器实例化");

    }

    // 这是BeanFactoryAware接口方法
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println("【BeanFactoryAware接口】调用BeanFactoryAware.setBeanFactory()");
        this.beanFactory = beanFactory;
    }

    // 这是BeanNameAware接口方法
    @Override
    public void setBeanName(String name) {
        System.out.println("【BeanNameAware接口】调用BeanNameAware.setBeanName()");
        this.beanName = name;
    }

    // 通过<bean>的init-method属性指定的初始化方法
    public void init() {
        System.out.println("【init-method】调用<bean>的init-method属性指定的初始化方法");
    }

    // 这是DiposibleBean接口方法
    @Override
    public void destroy() throws Exception {
        System.out.println("【DiposibleBean接口】调用DiposibleBean.destory()");
    }

    // 这是InitializingBean接口方法
    //bean 创建完成 属性值都赋值好以后
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("【InitializingBean接口】调用InitializingBean.afterPropertiesSet()");
    }


    @Override
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        String name = resolver.resolveStringValue("${os.name}");
        System.out.println("【EmbeddedValueResolverAware 接口setEmbeddedValueResolver() ==> " + name);
    }
}
