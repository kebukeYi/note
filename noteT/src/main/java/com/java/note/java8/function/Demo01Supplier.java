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

    public static void main(Strings[] args) {
        Strings msgA = "Hello ";
        Strings msgB = "World ";
        System.out.println(
                getString(() -> msgA + msgB)
//                getString(() -> System.out.println(msgA + msgB);)
        );
    }

    private static Strings getString(Supplier<Strings> stringSupplier) {
        return stringSupplier.get();
    }
}
 
