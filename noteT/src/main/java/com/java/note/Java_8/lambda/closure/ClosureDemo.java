package com.java.note.Java_8.lambda.closure;

import java.util.function.Supplier;

/**
 * @Author : mmy
 * @CreatTime : 2020/3/31  22:19
 * @Description
 */
public class ClosureDemo {

    public static void main(String[] args) {
        int n = getNumber().get();
        System.out.println(n);
    }

    private static Supplier<Integer> getNumber() {
        /*
        闭包会提升 方法变量的生命周期
        num 变量在这个方法结束之后不会被销毁
         */
        int num = 10;
        return () -> {
            return num;
        };
    }


}
