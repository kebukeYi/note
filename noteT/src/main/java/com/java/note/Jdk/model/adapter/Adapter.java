package com.java.note.Jdk.model.adapter;

/**
 * @Author : mmy
 * @Creat Time : 2020/7/1  21:54
 * @Description 适配器 ：连接USB  + 连接网线
 */
public class Adapter extends Adaptee implements Net2USB {

    @Override
    public void handlerRequestz() {
        super.request();
    }
}
