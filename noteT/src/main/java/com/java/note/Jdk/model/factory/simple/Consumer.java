package com.java.note.Jdk.model.factory.simple;


/**
 * @Author : mmy
 * @Creat Time : 2020/6/27  17:20
 * @Description
 */
public class Consumer {
    public static void main(Strings[] args) {
        //1 .接口，所有的实现类！
//        Car wuling = new Wuling();
//        Car tesla = new Tesla();

        //2.
        Car tesla = CarFactory.getCar("1");
        Car wuling = CarFactory.getCar("5");


        tesla.getName();
        wuling.getName();
    }

}
