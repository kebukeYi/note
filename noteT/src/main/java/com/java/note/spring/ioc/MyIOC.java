package com.java.note.spring.ioc;

import com.java.note.spring.config.MainConfig;
import com.java.note.spring.service.PeopleService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/4  21:03
 * @Description
 */
public class MyIOC {

    @SuppressWarnings("resource")
    public static void main(Strings[] args) {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(MainConfig.class);
//        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(MainConfigOfAOP.class);
//        String[] beanNamesForType = ac.getBeanDefinitionNames();
//        for (String str : beanNamesForType) {
//            System.out.println(str);
//        }

//        MathCalculator bean = ac.getBean(MathCalculator.class);
//        bean.div(1, 2);

        PeopleService peopleService = ac.getBean(PeopleService.class);
        peopleService.insertPeople();
    }


}
