package com.java.note.Jdk.model.prototype.demo02;

import java.util.Date;

/**
 * @Author : mmy
 * @Creat Time : 2020/7/1  19:48
 * @Description
 */
public class Main {
    public static void main(String[] args) throws CloneNotSupportedException {
        Date date = new Date();

        com.java.note.Jdk.model.prototype.demo02.Video video = new com.java.note.Jdk.model.prototype.demo02.Video("name01", date);
        System.out.println(video);
        System.out.println(video.hashCode());

        com.java.note.Jdk.model.prototype.demo02.Video clone = (Video) video.clone();
        System.out.println(clone);
        System.out.println(clone.hashCode());

        date.setTime(213545644);
        System.out.println(video);
        System.out.println(clone);
    }
}
