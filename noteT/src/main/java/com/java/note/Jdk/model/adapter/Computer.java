package com.java.note.Jdk.model.adapter;

/**
 * @Author : mmy
 * @Creat Time : 2020/7/1  20:01
 * @Description 客户端 想上网 插不上网线
 */
public class Computer {

    public void net(Net2USB net2USB) {
        net2USB.handlerRequestz();
    }

    public static void main(String[] args) {
        Computer computer = new Computer();//电脑
        Adaptee adaptee = new Adaptee();//网线
        Adapter adapter = new Adapter(); //转接器 1
        Adapter2 adapter2 = new Adapter2(adaptee); //转接器 2

        computer.net(adapter);
        computer.net(adapter2);
    }

}
