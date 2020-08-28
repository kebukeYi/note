package com.java.note.Jdk.model.factory.abstractF;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/27  18:50
 * @Description
 */
public class HuaWeiPhone implements PhoneProduct {

    @Override
    public void start() {
        System.out.println("开启华为手机");

    }

    @Override
    public void shutdown() {
        System.out.println("关闭华为手机");

    }

    @Override
    public void callup() {
        System.out.println("华为手机打电话");

    }

    @Override
    public void setting() {
        System.out.println("设置华为手机");

    }
}
