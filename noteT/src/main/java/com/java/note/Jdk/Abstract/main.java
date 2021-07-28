package com.java.note.Jdk.Abstract;

/**
 * @author : kebukeyi
 * @date :  2021-07-09 22:07
 * @description :
 * @question :
 * @usinglink :
 **/
public class main {

    public static void main(String[] args) {
        final SubOne subOne = new SubOne(2);
        //实例的实例变量
        subOne.server.serverId = 2;
        //实例变量
        subOne.abstractNum = 3;
        //静态变量
        SubOne.staticAbstractNum = 1;
        System.out.println(subOne.server.serverId);
        System.out.println(subOne.abstractNum);
        System.out.println(AbstractFat.staticAbstractNum);
        System.out.println("=====================");
        final SubTwo subTwo = new SubTwo();
        System.out.println(subTwo.abstractNum);
        System.out.println(subTwo.server.serverId);
    }
}
 
