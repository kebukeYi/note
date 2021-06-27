package com.java.note.spring.ioc;

import org.apache.ibatis.annotations.Select;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/24  14:22
 * @Description 生成特殊的Bean :  现在可以生成MyBatis的 *Mapper 代理对象 ，然后可以装配在Spring 中，再在service 层 DI ；
 */
//@Component
//public class MyFactoryBean implements FactoryBean {
public class MyFactoryBean {

    private Class mapperInterface;

    public MyFactoryBean(Class mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    /**
     * 如何创建 new  MyFactorBean(PeopleMapper.class)?
     * 如何创建 new  MyFactorBean(UserMapper.class)?
     * 思路是 ：在MyBeanFactoryPostProcessor 创建 BeanDefinition 然后交给Spring 容器实例化，
     */
//使用Spring提供的 FactoryBean(工厂Bean);
//默认获取到的是工厂bean调用 getObject创建的对象
//要获取工厂Bean本身,我们需要给Id前面加一个&     &color Factory Bean
    public Object getObject() throws Exception {
        Object o = Proxy.newProxyInstance(MyFactoryBean.class.getClassLoader(), new Class[]{mapperInterface}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //执行Object 的方法的话，转到自己的方法
                if (Object.class.equals(method.getDeclaringClass())) {
                    return method.invoke(this, args);
                }
                String[] value = method.getAnnotation(Select.class).value();
                return null;
            }
        });
        return o;
    }


    public Class<?> getObjectType() {
        return mapperInterface;
    }



    public boolean isSingleton() {
        return true;
    }
}
