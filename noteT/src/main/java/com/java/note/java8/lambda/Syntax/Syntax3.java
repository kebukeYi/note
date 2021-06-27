package com.java.note.java8.lambda.Syntax;

import com.java.note.java8.lambda.interfaces.LambdaSingleReSingleParamter;

/**
 * @Author : mmy
 * @Creat Time : 2020/3/31  19:21
 * @Description
 */
public class Syntax3 {

    /*
   方法引用
   可以将一个Lambda表达式的实现指向一个已经实现的方法
   语法：方法的隶属者   ::
   注意：参数数量 类型 一致 ；返回值的类型需要一致
  */
    public static void main(String[] args) {

        LambdaSingleReSingleParamter lambdaSingleReSingleParamter = a -> a * 2;

        LambdaSingleReSingleParamter lambdaSingleReSingleParamter1 = a -> change(a);

        LambdaSingleReSingleParamter lambdaSingleReSingleParamter2 = Syntax3::change;//静态方法 ->  类::方法

        System.out.println(lambdaSingleReSingleParamter2.test(2));

    }

    public static int change(int a) {
        return a * 2;
    }

}
