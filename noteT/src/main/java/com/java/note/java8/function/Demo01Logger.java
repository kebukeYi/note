package com.java.note.java8.function;

/**
 * <h3>note</h3>
 *
 * @author : kebukeyi
 * @date :  2021-04-13 14:42
 * @description :
 **/
public class Demo01Logger {

    public static void main(Strings[] args) {
        Strings msgA = "Hello ";
        Strings msgB = "World ";
        Strings msgC = "Java";
        //无论级别 level 是否满足要求，作为 log 方法的第二个参数，三个字符串一定会首先被拼接并传入方法内，然后才会进行级别判断。
        // 如果级别不符合要求，那么字符串的拼接操作就白做了，存在性能浪费。
        log(1, msgA + msgB + msgC);
    }

    private static void log(int level, Strings message) {
        if (level == 1)
            System.out.println(message);
    }
}
 
