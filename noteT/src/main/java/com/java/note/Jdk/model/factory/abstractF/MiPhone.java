package com.java.note.Jdk.model.factory.abstractF;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/27  18:49
 * @Description
 */
public class MiPhone implements PhoneProduct {
    @Override
    public void start() {
        System.out.println("开启小米手机");
    }

    @Override
    public void shutdown() {
        System.out.println("关闭小米手机");
    }

    @Override
    public void callup() {
        System.out.println("小米打电话");
    }

    @Override
    public void setting() {
        System.out.println("设置小米手机");
    }
}
