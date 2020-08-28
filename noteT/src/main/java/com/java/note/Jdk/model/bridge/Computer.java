package com.java.note.Jdk.model.bridge;

/**
 * @Author : mmy
 * @Creat Time : 2020/7/1  22:35
 * @Description 多使用组合方式
 * 抽象电脑类
 */
public abstract class Computer {

    //组合  品牌

    protected Brand brand;

    public Computer(Brand brand) {
        this.brand = brand;
    }

    public void info() {
        brand.info();
    }
}

class DeskTop extends Computer {

    public DeskTop(Brand brand) {
        super(brand);
    }

    @Override
    public void info() {
        super.info();
        System.out.println("台式机");
    }
}

class LapTop extends Computer {

    public LapTop(Brand brand) {
        super(brand);
    }

    @Override
    public void info() {
        super.info();
        System.out.println("笔记本");
    }
}