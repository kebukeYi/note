package com.java.note.spring.ioc;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/18  11:18
 * @Description 时机： bean被定义，但是还没被初始化；
 * 演示Bean工厂 后置处理器接口方法 ，并不支持 add beanDefinition;  思路 ： MyBeanDefinitionRegister
 */
@Component
public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    public MyBeanFactoryPostProcessor() {
        super();
        System.out.println("这是[BeanFactoryPostProcessor]实现类构造器！！");
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        System.out.println("[BeanFactoryPostProcessor]调用postProcessBeanFactory方法");

//        BeanDefinition bd = beanFactory.getBeanDefinition("people");
//        System.out.println(bd);
//        bd.getPropertyValues().addPropertyValue("name", "mmy");

        //非常重要的思想  但是无法这个beanDefinition 放到容器中；思路： 放到  MyBeanDefinitionRegister  中；
//        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition();
//        BeanDefinition beanDefinition = builder.getBeanDefinition();
//        beanDefinition.setBeanClassName(MyFactoryBean.class.getName());
//        //会调用 MyFactoryBean 的构造方法，然后生成 PeopleMapper的代理对象
//        beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(PeopleMapper.class);

    }
}
