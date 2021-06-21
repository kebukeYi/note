package com.java.note.java8.function;

import java.util.function.Function;

/**
 * @author : kebukeyi
 * @date :  2021-04-13 16:19
 * @description : java.util.function.Function<T,R> 接口用来根据一个类型的数据得到另一个类型的数据，前者称为前置条件，后者称为后置条件
 **/
public class DemoFunctionApply {

    public static void main(Strings[] args) {
        method(s -> Integer.parseInt(s));
    }

    private static void method(Function<Strings, Integer> function) {
        int num = function.apply("10");
        System.out.println(num + 20);
    }
}
 
