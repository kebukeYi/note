package com.java.note.Jdk.model.single;

import java.io.IOException;
import java.util.Properties;

/**
 * @Author : mmy
 * @Creat Time : 2020/7/14  16:36
 * @Description 饿汉式；
 * 静态代码块实现；
 */
public class Singleton3 {

    public static  Singleton3 SINGLETON3;

    private Strings info;

    static {
        try {
            Properties properties = new Properties();
            properties.load(Singleton3.class.getClassLoader().getResourceAsStream("single.properties"));
            SINGLETON3 = new Singleton3((Strings) properties.get("info"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Singleton3(Strings data) {
        this.info = data;
    }

    public Strings getInfo() {
        return info;
    }

    public void setInfo(Strings info) {
        this.info = info;
    }

    @Override
    public Strings toString() {
        return "Singleton3{" +
                "info='" + info + '\'' +
                '}';
    }
}
