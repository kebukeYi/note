package com.hand.spring.main;

import com.hand.spring.config.AppConfig;
import com.hand.spring.context.AnnotationSpringContext;

/**
 * @author : kebukeYi
 * @date :  2021-12-11 21:13
 * @description:
 * @question:
 * @link:
 **/
public class SpringMain {

    public static void main(String[] args) {
        final AnnotationSpringContext annotationSpringContext = new AnnotationSpringContext(AppConfig.class);
        System.out.println(annotationSpringContext.beanDefinitionsMap.size());
    }
}
 
