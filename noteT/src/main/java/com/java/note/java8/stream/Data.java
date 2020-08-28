package com.java.note.java8.stream;

import com.java.note.java8.bean.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author : mmy
 * @Creat Time : 2020/3/31  11:42
 * @Description
 */
public class Data {

    public static List<Person> getPersonList() {
        List<Person> list = new ArrayList<>();
        list.add(new Person("小明", 18, 96));
        list.add(new Person("小兰", 23, 98));
        list.add(new Person("小红", 21, 65));
        list.add(new Person("小崩", 20, 91));
        list.add(new Person("小绿", 19, 88));
        list.add(new Person("小d", 16, 68));
        list.add(new Person("小g", 14, 87));

        list.add(new Person("小r", 15, 83));
        list.add(new Person("小r", 15, 83));

        return list;
    }


}
