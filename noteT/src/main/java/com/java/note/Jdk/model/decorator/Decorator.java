package com.java.note.Jdk.model.decorator;

/**
 * @Author : mmy
 * @Creat Time : 2020/8/11  16:02
 * @Description 装修基本类
 */
public class Decorator implements IDecorator {

    @Override
    public void decorate() {
        System.out.println(" 水电装修、天花板以及粉刷墙。。。");
        IDecorator iDecorator = new Decorator();
        iDecorator.text();
    }


}
