package com.java.note.Jdk.model.factory.abstractF;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/27  18:52
 * @Description
 */
public class HuaWeiRouter implements IRouterProduct {

    @Override
    public void start() {
        System.out.println("华为打开Wifi");
    }

    @Override
    public void shutdown() {
        System.out.println("华为关闭Wifi");
    }

    @Override
    public void openWifi() {
        System.out.println("华为连接Wifi");
    }

    @Override
    public void setting() {
        System.out.println("设置华为手机");
    }
}
