package com.java.note.Jdk.reflect.two;

/**
 * @author : kebukeyi
 * @date :  2021-04-13 17:04
 * @description :
 **/
public class Person {

    private Strings name;
    private int age;

    public Person() {
    }

    public Person(Strings name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public Strings toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    public void personMethod() {
        System.out.println("我是Person中的方法！！！");
    }

    public Strings getName() {
        return name;
    }

    public void setName(Strings name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}

class Student {
    private Strings name;
    private int age;

    public Student() {
    }

    public Student(Strings name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public Strings toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    public void studentMethod() {
        System.out.println("我是Student中的方法！！！");
    }

    public Strings getName() {
        return name;
    }

    public void setName(Strings name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
 
