package com.java.note.Jdk.model.prototype.demo01;

import java.util.Date;

/**
 * @Author : mmy
 * @Creat Time : 2020/7/1  19:33
 * @Description 克隆对象
 */
public class Consumer {


    public static void main(String[] args) throws CloneNotSupportedException {
        Date date = new Date();

        Video video = new Video("name01", date);
        System.out.println(video);
        System.out.println(video.hashCode());

        Video clone = (Video) video.clone();
        System.out.println(clone);
        System.out.println(clone.hashCode());

        video.setName("02");
        date.setTime(213545644);
        System.out.println(video);
        System.out.println(clone);
    }
}
