package com.java.note.Jdk.subclass;

/**
 * @author : kebukeYi
 * @date :  2021-08-08 19:46
 * @description:
 * @question:
 * @link:
 **/
public class DefaultInterfacelmpl implements DefaultInterface {

    @Override
    public String getName() {
        final int static_super_int = DefaultInterface.static_super_int;
        System.out.println(static_super_int);
        getStatic_super_int();
        return "DefaultInterfacelmpl 实例化";
    }
}
 
