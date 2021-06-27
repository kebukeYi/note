package com.java.note.Jdk.Annotation;

import java.lang.annotation.Annotation;

/**
 * @author : kebukeyi
 * @date :  2021-04-13 17:39
 * @description : 其实就是在内存中生成了一个该注解接口的子类实现对象
 **/
public class proImpl implements pro {

    @Override
    public String className() {
        return this.className();
    }

    @Override
    public String methodName() {
        return this.methodName();
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return this.annotationType();
    }
}
 
