package com.java.note.java8.function;

import java.util.function.Supplier;

/**
 * <h3>note</h3>
 *
 * @author : kebukeyi
 * @date :  2021-04-13 14:54
 * @description : 默认是有返回值的
 **/
public class Demo01Supplier {

    public static void main(String[] args) {
        String msgA = "Hello ";
        String msgB = "World ";
        System.out.println(
                getString(() -> msgA + msgB)
//                getString(() -> System.out.println(msgA + msgB);)
        );
    }

    private static String getString(Supplier<String> stringSupplier) {
        return stringSupplier.get();
    }
}
 
