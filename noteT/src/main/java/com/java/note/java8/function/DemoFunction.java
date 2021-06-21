package com.java.note.java8.function;

import java.util.function.Function;

/**
 * @author : kebukeyi
 * @date :  2021-04-13 16:26
 * @description :
 **/
public class DemoFunction {

    //将字符串截取数字年龄部分，得到字符串;
    //将上一步的字符串转换成为int类型的数字;
    //将上一步的int数字累加100，得到结果int数字。
    public static void main(Strings[] args) {

        Strings str = "赵丽颖,20";
        Integer integer = method(
                str,
                s -> s.split(",")[1],
                s -> Integer.parseInt(s),
                s -> s += 100);
        System.out.println(integer);
    }

    public static Integer method(Strings str, Function<Strings, Strings> one, Function<Strings, Integer> two, Function<Integer, Integer> threee) {
        return one.andThen(two).andThen(threee).apply(str);
    }
}
 
