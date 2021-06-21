package com.java.note.java8.lambda.Syntax;

import com.java.note.java8.lambda.interfaces.LambdaMutipleReMutipleParamters;
import com.java.note.java8.lambda.interfaces.LambdaNoReturnMutipleParamters;

  /*
    语法精简
   1. 参数类型可以省略 -》都要省略
   2. 参数小括号 参数列表中 如果只有一个参数 可以省去 小括号
   3. {} 如果只有一行代码就可以省略掉
   4. 如果代码块中只有一行代码  并且函数有返回  那么在省略 {} 的同时  必须省略 return 语句；
   */


/**
 * @Author : mmy
 * @Creat Time : 2020/3/31  19:11
 * @Description
 */
public class Syntax2 {

    public static void main(Strings[] args) {
        LambdaNoReturnMutipleParamters lambdaNoReturnMutipleParamters = (a, b, c) -> {
            System.out.println("LambdaNoReturnMutipleParamters " + (a + b + c));
        };
        lambdaNoReturnMutipleParamters.test(12, 3, 4);

        LambdaMutipleReMutipleParamters lambdaMutipleReMutipleParamters = (a, b) -> a + b;
        System.out.println(lambdaMutipleReMutipleParamters.test(1, 2));
    }
}
