package com.java.note.Jdk.model.factory.method;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/27  17:20
 * @Description
 */
public class Tesla implements Car {
    @Override
    public void getName() {
        System.out.println("特斯拉");
    }
}
