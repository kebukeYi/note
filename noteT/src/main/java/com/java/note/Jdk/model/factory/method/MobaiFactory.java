package com.java.note.Jdk.model.factory.method;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/27  18:24
 * @Description
 */
public class MobaiFactory implements CarFactory {
    @Override
    public Car getCar() {
        return new Mobai();
    }
}
