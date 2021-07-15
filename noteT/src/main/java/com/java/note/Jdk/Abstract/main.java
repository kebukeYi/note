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
        subOne.server.serverId = 2;
        System.out.println(subOne.abstractNum);
        System.out.println(subOne.server.serverId);
        System.out.println("=====================");
        final SubTwo subTwo = new SubTwo();
        System.out.println(subTwo.abstractNum);
        System.out.println(subTwo.server.serverId);
    }
}
 
