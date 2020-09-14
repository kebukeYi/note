package com.java.note.Jdk.proxy.main;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/14  15:00
 * @Description
 */
public class MyController {


    @MAutowried
    private MyService myService;


    public MyService getMyService() {
        return myService;
    }

//    public void setMyService(MyService myService) {
//        this.myService = myService;
//    }
}
