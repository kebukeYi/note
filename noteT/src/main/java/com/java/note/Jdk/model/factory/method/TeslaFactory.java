package com.java.note.Jdk.model.factory.method;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/27  18:21
 * @Description
 */
public class TeslaFactory implements CarFactory {


    @Override
    public Car getCar() {
        return new Tesla();
    }
}
