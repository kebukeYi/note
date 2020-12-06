package com.java.note.Jdk.enums;

/**
 * @Author : mmy
 * @Creat Time : 2020/10/18  下午 1:07
 * @Description
 */
public enum Day2 {

    MONDAY("星期一"),
    TUESDAY("星期二"),
    WEDNESDAY("星期三"),
    THURSDAY("星期四"),
    FRIDAY("星期五"),
    SATURDAY("星期六"),
    SUNDAY("星期日");//记住要用分号结束

    private String desc;//中文描述

    /**
     * 私有构造,防止被外部调用
     *
     * @param name
     */
    Day2(String name) {
        this.desc = name;
    }

    /**
     * 定义方法,返回描述,跟常规类的定义没区别
     *
     * @return
     */
    public String getDesc() {
        return desc;
    }

    /**
     * 覆盖
     *
     * @return
     */
    @Override
    public String toString() {
        return desc;
    }


    public static void main(String[] args) {
        for (Day2 day : Day2.values()) {
            System.out.println("name:" + day.name() + ", desc:" + day.getDesc() + ",day.toString()" + day.toString());
        }
    }


}
