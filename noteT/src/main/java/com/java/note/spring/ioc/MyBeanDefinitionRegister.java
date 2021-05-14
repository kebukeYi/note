package com.java.note.spring.ioc;

import com.java.note.spring.annotation.MyMapperScan;
import com.java.note.spring.mapper.PeopleMapper;
import com.java.note.spring.mapper.UserMapper;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/24  15:09
 * @Description: 源头 向容器中注入bean
 */
public class MyBeanDefinitionRegister implements ImportBeanDefinitionRegistrar {

    /**
     * importingClassMetadata 当前类的注解信息
     * registry  注册bean
     * importBeanNameGenerator
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry, BeanNameGenerator importBeanNameGenerator) {
        System.out.println("MyBeanDefinitionRegister ");
        Map<String, Object> attributes = importingClassMetadata.getAnnotationAttributes(MyMapperScan.class.getName());
        System.out.println(attributes.get("value"));
        //扫描Mapper ，未完成
        List<Class> mappers = new ArrayList<>();
        mappers.add(PeopleMapper.class);
        mappers.add(UserMapper.class);

        for (Class mapper : mappers) {
//            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition();
//            BeanDefinition beanDefinition = builder.getBeanDefinition();
//
//            beanDefinition.setBeanClassName(MyFactoryBean.class.getName());
//
//            //会调用 MyFactoryBean 的构造方法，然后生成 ****Mapper的代理对象
//            beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(mapper);
//            registry.registerBeanDefinition(mapper.getName(), beanDefinition);
        }

    }
}
