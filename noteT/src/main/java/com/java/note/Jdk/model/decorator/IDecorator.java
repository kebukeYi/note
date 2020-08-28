package com.java.note.Jdk.model.decorator;

/**
 * @Author : mmy
 * @Creat Time : 2020/8/11  16:01
 * @Description 基本装饰接口
 */
public interface IDecorator {

    /**
     * 装修方法
     */
    void decorate();

    default void text() {
        System.out.println(IDecorator.class.getClassLoader());
    }

}
