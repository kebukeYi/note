package com.java.note.java8.lambda;

import com.java.note.java8.bean.Person;
import com.java.note.java8.lambda.interfaces.Comparator;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

/**
 * @Author : mmy
 * @Creat Time : 2020/3/31  16:14
 * @Description
 */
public class Lambda {
    /*
    Lambda:匿名函数,，可以对一个接口进行非常简洁的实现，但是最关键的基本要求是： 函数式接口 +  接口中只能有一个要实现的抽象方法即没有加  default  修饰的方法；
    允许把函数作为一个方法的参数(函数作为参数传递进方法中。
    方法引用-方法引用提供了非常有用的语法,可以直接引用已有Java类或对象(实例)的方法或构造器。
    与lambda联合使用 方法引用可以使语言的构造更紧凑简洁，减少冗余代码。

    @FunctionalInterface  修饰于 只有一个抽象方法的注解
     */


    public static void main(String[] args) {
        //1. 接口实现类对象
        MyComparetor myComparetor = new MyComparetor();
        //2.使用匿名内部类
        Comparator comparator = new Comparator() {
            @Override
            public int compare(int a, int b) {
                return a - b;
            }
        };
        // 3. 使用lambda
        Comparator comparator1 = (a, b) -> a - b;
    }
}

class MyComparetor implements Comparator {
    @Override
    public int compare(int a, int b) {
        return a - b;
    }
}
