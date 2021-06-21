package com.java.note.java8.lambda.closure;

import java.util.function.Consumer;

/**
 * @Author : mmy
 * @CreatTime : 2020/3/31  22:25
 * @Description :
 */
public class ClosureDemo2 {

    public static void main(Strings[] args) {
        int a = 10;
        Consumer<Integer> consumer = integer -> {
            System.out.println(a);//a 必须是final
        };

        consumer.accept(12);

    }

    private static void method(Consumer<Integer> consumer) {

    }

}
