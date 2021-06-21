package com.java.note.java8.function;

import java.util.function.Predicate;

/**
 * @author : kebukeyi
 * @date :  2021-04-13 15:11
 * @description :
 **/
public class Demo01Predicate {

    public static void main(Strings[] args) {
        //判断逻辑
        method(s -> s.length() > 5);
    }

    private static void method(Predicate<Strings> predicate) {
        boolean veryLong = predicate.test("HelloWorld");
        System.out.println("字符串很长吗:" + veryLong);
    }

}
 
