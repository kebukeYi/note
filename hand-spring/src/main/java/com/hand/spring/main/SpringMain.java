package com.hand.spring.main;

import com.hand.spring.config.AppConfig;
import com.hand.spring.context.AnnotationSpringContext;
import com.hand.spring.service.IUserService;
import com.hand.spring.service.OrderService;
import com.hand.spring.service.UserService;

/**
 * @author : kebukeYi
 * @date :  2021-12-11 21:13
 * @description: 来自B站的手写Spring之周瑜
 * @question:
 * @link:
 **/
public class SpringMain {

    public static void main(String[] args) {
        final AnnotationSpringContext annotationSpringContext = new AnnotationSpringContext(AppConfig.class);
        System.out.println("BeanDefinitionMap 容器中的BD数量：" + annotationSpringContext.beanDefinitionsMap.size());
        System.out.println("singletonObjects 容器中的Bean数量：" + annotationSpringContext.singletonObjects.size());
        //原型 Bean
        final OrderService orderService = (OrderService) annotationSpringContext.getBean("orderService");
        final OrderService orderServices = (OrderService) annotationSpringContext.getBean("orderService");
        // System.out.println(orderService);
        // System.out.println(orderServices);
        // orderServices.getUserService();
        //单例 Bean
        final IUserService userService = (IUserService) annotationSpringContext.getBean("userService");
        final IUserService userServices = (IUserService) annotationSpringContext.getBean("userService");
        // System.out.println(userService);
        // System.out.println(userServices);
        userService.proxyBean();
    }
}
 
