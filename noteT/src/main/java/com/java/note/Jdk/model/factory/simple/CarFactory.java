package com.java.note.Jdk.model.factory.simple;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/27  18:30
 * @Description
 */
public class CarFactory {

    public static Car getCar(Strings car) {
        return new Wuling();
    }
}
