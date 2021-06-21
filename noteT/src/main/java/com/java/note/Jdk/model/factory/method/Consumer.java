package com.java.note.Jdk.model.factory.method;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/27  17:20
 * @Description
 */
public class Consumer {
    public static void main(Strings[] args) {
        Car car = new WulingFactory().getCar();
        Car car1 = new TeslaFactory().getCar();

        car.getName();
        car1.getName();
    }

}
