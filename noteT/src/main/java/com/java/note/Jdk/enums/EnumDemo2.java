package com.java.note.Jdk.enums;

/**
 * @Author : mmy
 * @Creat Time : 2020/10/18  下午 1:19
 * @Description 枚举无法再次继承就选择 实现多个接口；
 */
interface food {
    void eat();

}

interface sport {
    void run();
}


enum EnumDemo2 implements food, sport {

    FOOD,
    SPORT,
    ; //分号分隔

    @Override
    public void eat() {
        System.out.println("eat.....");
    }

    @Override
    public void run() {
        System.out.println("run.....");
    }
}
