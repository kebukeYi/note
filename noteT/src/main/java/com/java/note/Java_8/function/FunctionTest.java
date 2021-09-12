package com.java.note.Java_8.function;

import org.springframework.data.relational.core.sql.In;

import java.util.function.Function;

/**
 * @author : kebukeYi
 * @date :  2021-09-01 09:55
 * @description:
 * @question:
 * @link:
 **/
public class FunctionTest {

    public static void main(String[] args) {
        final FunctionTest functionTest = new FunctionTest();

        final Function<Integer, Integer> function = new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer o) {
                return o;
            }
        };

        final Function<Integer, String> function1 = new Function<Integer, String>() {
            @Override
            public String apply(Integer o) {
                return "result : " + o;
            }
        };
        final int compute = functionTest.compute(2, function);
        System.out.println(compute);

        // 用lambda的方式实现
        Function<Integer, Integer> lambdaFunc = o -> o * o;
        Function<Integer, String> function2 = lambdaFunc.andThen(o -> "result is " + o);
        String apply1 = function2.apply(2);
        System.out.println(apply1);
    }

    public int compute(int a, Function<Integer, Integer> f) {
        return f.apply(a);
    }
}
 
