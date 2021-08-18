package com.java.note.Jdk.subclass;

/**
 * @author : kebukeYi
 * @date :  2021-08-08 19:38
 * @description:
 * @question:
 * @link:
 **/
public class SubClass extends SuperClass {

    @Override
    public int getSuper_int() {
        return super.getSuper_int();
    }

    public static void main(String[] args) {
        final SubClass subClass = new SubClass();
        subClass.getSuper_int();

        final DefaultInterfacelmpl defaultInterfacelmpl = new DefaultInterfacelmpl();
        System.out.println(defaultInterfacelmpl.getName());
    }

}
 
