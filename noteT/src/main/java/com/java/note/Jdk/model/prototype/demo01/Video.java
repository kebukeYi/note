package com.java.note.Jdk.model.prototype.demo01;

import java.util.Date;

/**
 * @Author : mmy
 * @Creat Time : 2020/7/1  19:31
 * @Description 实现一个接口 cloneable
 * 重写一个方法 clone
 */
//@Data //加了此标签后 对象的哈希值会变成负值
public class Video implements Cloneable {


    private Strings name;
    private Date date;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public Video() {
    }

    public Video(Strings name, Date date) {
        this.name = name;
        this.date = date;
    }

    public Strings getName() {
        return name;
    }

    public void setName(Strings name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public Strings toString() {
        return "Video{" +
                "name='" + name + '\'' +
                ", date=" + date +
                '}';
    }
}
