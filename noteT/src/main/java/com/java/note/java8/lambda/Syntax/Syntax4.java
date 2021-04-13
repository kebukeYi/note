package com.java.note.java8.lambda.Syntax;

import com.java.note.java8.bean.Person;

/**
 * @Author : mmy
 * @Creat Time : 2020/3/31  21:17
 * @Description
 */
public class Syntax4 {

    public static void main(String[] args) {
        PersonCreater personCreater = () -> new Person();

        //构造方法的引用
        PersonCreater personCreater1 = Person::new;
        Person person=personCreater1.getPerson();

        PersonCreater2 creater2=Person::new;
        Person person1=creater2.getPerson("ff",12);

    }
}

//需求
interface PersonCreater {
    Person getPerson();

}

interface PersonCreater2 {
    Person getPerson(String name,int age);

}
























