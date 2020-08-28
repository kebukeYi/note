package com.java.note.Jdk.model.adapter;

/**
 * @Author : mmy
 * @Creat Time : 2020/7/1  22:16
 * @Description 组合使用
 */
public class Adapter2 implements Net2USB {

    private Adaptee adaptee;

    public Adapter2(Adaptee adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public void handlerRequestz() {
        adaptee.request();
    }
}
