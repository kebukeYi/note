package com.java.note.Jdk.proxy.main;

import java.util.stream.Stream;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/14  16:37
 * @Description
 */
public class MyMain2 {

    public static void main(String[] args) {

        MyController controller = new MyController();
        //获取class 对象
        Class<? extends MyController> aClass = controller.getClass();

//        aClass.getAnnotatedInterfaces();

        Stream.of(aClass.getDeclaredFields()).forEach(fired -> {
            //获取是否有注解
            Autowried autowried = fired.getAnnotation(Autowried.class);
            if (autowried != null) {
                fired.setAccessible(true);
                //获取当前类型
                Class<?> type = fired.getType();
                try {
                    //创建对象
                    Object o = type.newInstance();
                    //传入参数
                    fired.set(controller, o);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });

        System.out.println(controller.getMyService());
    }
}
