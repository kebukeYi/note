package com.hand.spring.annoation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author : kebukeYi
 * @date :  2021-12-11 21:11
 * @description: 扫描组件注解
 * @question:
 * @link:
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ComponentScan {

    //定义组件类路径
    String value();

}
 
