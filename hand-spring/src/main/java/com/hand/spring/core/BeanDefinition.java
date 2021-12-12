package com.hand.spring.core;

/**
 * @author : kebukeYi
 * @date :  2021-12-11 22:01
 * @description:
 * @question:
 * @link:
 **/
public class BeanDefinition {

    private Class beanClass;
    private String scope;

    public Class getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class beanClass) {
        this.beanClass = beanClass;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
 
