package com.java.note.Jdk.model.agent;

/**
 * @Author : mmy
 * @Creat Time : 2020/7/1  23:18
 * @Description 房东
 */
public class Host implements Rent {

    @Override
    public void rent() {
        System.out.println("租房子");
    }

}
