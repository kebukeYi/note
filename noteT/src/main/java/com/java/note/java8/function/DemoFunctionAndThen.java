package com.java.note.java8.function;

import java.util.function.Function;

/**
 * @author : kebukeyi
 * @date :  2021-04-13 16:23
 * @description :
 **/
public class DemoFunctionAndThen {

    public static void main(String[] args) {
        //请注意，Function的前置条件泛型和后置条件泛型可以相同。
        method(
                str -> Integer.parseInt(str) + 10,
                i -> i *= 10
        );
    }

    private static void method(Function<String, Integer> one, Function<Integer, Integer> two) {
        int num = one.andThen(two).apply("10");
        System.out.println(num);
    }
}
 
