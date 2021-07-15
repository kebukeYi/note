package com.java.note.Java_8.lambda.exercise;

import com.java.note.Java_8.bean.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * @Author : mmy
 * @Creat Time : 2020/3/31  21:34
 * @Description
 */
public class Exercise2 {

    public static void treeSet(String[] args) {
        //使用lambda 实现compar接口 并实例化一个对象
        TreeSet<Person> set = new TreeSet<Person>(((o1, o2) -> {
            if (o1.getAge() >= o2.getAge()) {
                return -1;
            } else {
                return 1;
            }
        }
        ));

        set.add(new Person("小明", 18, 96));
        set.add(new Person("小兰", 23, 98));
        set.add(new Person("小红", 21, 65));
        set.add(new Person("小崩", 20, 91));
        set.add(new Person("小绿", 19, 88));
        set.add(new Person("小d", 16, 68));
        set.add(new Person("小g", 14, 87));

        set.add(new Person("小r", 15, 83));
        set.add(new Person("小r", 15, 83));

        System.out.println(set);
    }

    public static void main(String[] args) {
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

        list.forEach(System.out::print);


        list.forEach(ele -> {
            if (ele.getScore() > 60) {
                System.out.println(ele);
            }
        });

    //删除元素
        list.removeIf(ele -> ele.getScore() < 95);
    }

}
