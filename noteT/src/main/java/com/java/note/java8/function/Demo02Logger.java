package com.java.note.java8.function;

/**
 * <h3>note</h3>
 *
 * @author : kebukeyi
 * @date :  2021-04-13 14:44
 * @description : Lambda的延迟执行
 * 有些场景的代码执行后，结果不一定会被使用，从而造成性能浪费。
 * 而Lambda表达式是延迟执行的，这正好可以 作为解决方案，提升性能。
 **/
public class Demo02Logger {
    public static void main(String[] args) {
        String msgA = "Hello ";
        String msgB = "World ";
        String msgC = "Java";

        //利用了 Lambda表达式 延迟执行的
        //只有当级别满足要求的时候，才会进行三个字符串的拼接;否则三个字符串将不会进行拼接。
        log(1, () -> msgA + msgB + msgC);
    }

    private static void log(int level, MessageBuilder messageBuilder) {
        if (level == 1)
            System.out.println(messageBuilder.builderMessage());
    }
}
 
