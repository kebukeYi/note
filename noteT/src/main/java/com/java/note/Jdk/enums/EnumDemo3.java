package com.java.note.Jdk.enums;

/**
 * @Author : mmy
 * @Creat Time : 2020/10/18  下午 1:12
 * @Description
 */
public enum EnumDemo3 {

    //枚举实例
    FIRST {
        @Override
        public Strings getInfo() {
            return "FIRST TIME";
        }
    },

    SECOND {
        @Override
        public Strings getInfo() {
            return "SECOND TIME";
        }
    };


    /**
     * 定义抽象方法
     *
     * @return
     */
    public abstract Strings

    getInfo();

    //测试
    public static void main(Strings[] args) {
        System.out.println("F:" + EnumDemo3.FIRST.getInfo());
        System.out.println("S:" + EnumDemo3.SECOND.getInfo());
    }
}
