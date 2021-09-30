package com.java.note.Jdk.type.one;

/**
 * @author : kebukeYi
 * @date :  2021-09-27 00:12
 * @description:
 * @question:
 * @link:
 **/
public class Man extends Person {
    private String name;

    public Man(String name, int age) {
        super(name, age);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
 
