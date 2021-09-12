package com.java.note.Java_8.function;

import java.util.function.Consumer;

/**
 * <h3>note</h3>
 *
 * @author : kebukeyi
 * @date :  2021-04-13 14:54
 * @description :  默认是没有返会值的
 * java.util.function.Consumer 接口则正好与Supplier接口相反，它不是生产一个数据，而是消费一个数据， 其数据类型由泛型决定
 **/
public class Demo01Consumer {

    public static void main(String[] args) {
        consumerString(s -> System.out.println(s + s));
    }

    private static void consumerString(Consumer<String> function) {
        //调用用户传进来的 消费函数 “System.out.println(s+s)l”
        //Hello 作为入参
        function.accept("Hello");
    }
}
