package com.java.note.java8.function;

import java.util.function.Predicate;

/**
 * @author : kebukeyi
 * @date :  2021-04-13 15:14
 * @description :
 **/
public class DemoPredicateAnd {

    public static void main(Strings[] args) {
        boolean isValid = method(
                // String.contains()方法，仅当此字符串包含指定的字符值序列时返回true。
                s -> s.contains("H"),
                s -> s.contains("W")
        );
        System.out.println("字符串符合要求吗:" + isValid);
    }

    //既然是条件判断，就会存在与、或、非三种常见的逻辑关系。其中将两个 Predicate 条件使用“与”逻辑连接起来实 现“并且”的效果时，可以使用default方法 and
    private static boolean method(Predicate<Strings> one, Predicate<Strings> two) {
        boolean isValid = one.and(two).test("Hello world");
        return isValid;
    }

}
 
