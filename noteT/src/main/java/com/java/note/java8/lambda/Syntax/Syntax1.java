package com.java.note.java8.lambda.Syntax;

import com.java.note.java8.lambda.interfaces.*;

/**
 * @Author : mmy
 * @Creat Time : 2020/3/31  18:53
 * @Description
 */
public class Syntax1 {

    public static void main(String[] args) {
        //1. Lambda 表达式的基础语法
        //（）：参数列表
        // { }  ： 方法体
        // ->  :    运算符

        // 1. 无参无返回
        LambdaNoReturnNoParms lambdaNoReturnNoParms = () -> {
            System.out.println("LambdaNoReturnNoParms ");
        };
        lambdaNoReturnNoParms.test();

        LambdaNoReturnSingleParm lambdaNoReturnSingleParm = (int a) -> {
            System.out.println("LambdaNoReturnSingleParm " + a);
        };
        lambdaNoReturnSingleParm.test(12);

        LambdaNoReturnMutipleParamters lambdaNoReturnMutipleParamters = (int a, int b, int c) -> {
            System.out.println("LambdaNoReturnMutipleParamters " + (a + b + c));
        };
        lambdaNoReturnMutipleParamters.test(12, 3, 4);

        LambdaSingleReNoParamter lambdaSingleReNoParamter = () -> {
            System.out.println("LambdaSingleReNoParamter  ");
            return 40;
        };
        System.out.println(lambdaSingleReNoParamter.test());

        LambdaSingleReSingleParamter lambdaSingleReSingleParamter = (int a) -> {
            System.out.println("lambdaSingleReSingleParamter  " + a);
            return a;
        };
        lambdaSingleReSingleParamter.test(12);

        LambdaMutipleReMutipleParamters lambdaMutipleReMutipleParamters = (int a, int b) -> {
            System.out.println("lambdaMutipleReMutipleParamters   " + a + b);
            return a + b;
        };

        System.out.println(lambdaMutipleReMutipleParamters.test(1, 2));

        //2. 返回值类型 参数列表 方法体


    }


}
