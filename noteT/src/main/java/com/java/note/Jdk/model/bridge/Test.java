package com.java.note.Jdk.model.bridge;

/**
 * @Author : mmy
 * @Creat Time : 2020/7/1  22:44
 * @Description
 */
public class Test {
    public static void main(Strings[] args) {
        //苹果 笔记本
        Computer computer = new LapTop(new Apple());
        computer.info();

        //联想台式机
        Computer computer1 = new DeskTop(new Leveon());
        computer1.info();
    }
}
