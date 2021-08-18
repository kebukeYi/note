package com.java.note.Jdk.subclass;

/**
 * @author : kebukeyi
 * @date :  2021-08-08 19:45
 * @description :
 * @question :
 * @usinglink :
 **/
public interface DefaultInterface {

    int static_super_int = 2;

    String getName();


    default int getStatic_super_int() {
        System.out.println("DefaultInterface 接口初始化");
        return static_super_int;
    }
}
