package com.java.note.java8.bean;

import lombok.Builder;
import lombok.Data;

import java.util.Objects;

/**
 * @Author : mmy
 * @Creat Time : 2020/3/31  11:31
 * @Description
 */
@Data
@Builder
public class Person {

    private String name;
    private int age;
    private int score;

    public Person() {
        System.out.println("无参方法执行");
    }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
        System.out.println("有参方法执行");
    }

    public Person(String name, int age, int score) {
        this.name = name;
        this.age = age;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public Person setName(String name) {
        this.name = name;
        return this;
    }

    public int getAge() {
        return age;
    }

    public Person setAge(int age) {
        this.age = age;
        return this;
    }

    public int getScore() {
        return score;
    }

    public Person setScore(int score) {
        this.score = score;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;//地址相同
        if (o == null || getClass() != o.getClass()) return false;//类型不一样
        Person person = (Person) o;
        return age == person.age &&//比较属性值
                score == person.score &&
                Objects.equals(name, person.name);
        //  return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age, score);
//        return new Random().nextInt(100);
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", score=" + score +
                '}';
    }

//    @Override
//    public int compareTo(Person o) {
//        return o.getScore() - this.getScore();//降序
//    }
}
