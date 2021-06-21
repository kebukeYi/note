package com.java.note.spring.annotation;

import com.java.note.spring.ioc.MyBeanDefinitionRegister;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/24  15:41
 * @Description
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(MyBeanDefinitionRegister.class)
public @interface MyMapperScan {

    Strings value() default "";
}
