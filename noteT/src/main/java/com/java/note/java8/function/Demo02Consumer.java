package com.java.note.java8.function;

import java.util.function.Consumer;

/**
 * <h3>note</h3>
 *
 * @author : kebukeyi
 * @date :  2021-04-13 14:22
 * @description :
 **/
public class Demo02Consumer {

    public static void main(Strings[] args) {
        //传入逻辑
        //数据在被调用的函数内部
        consumerString(
                // toUpperCase()方法，将字符串转换为大写
                s -> System.out.println(s.toUpperCase()),
                // toLowerCase()方法，将字符串转换为小写
                s -> System.out.println(s.toLowerCase()));

        //实战
        Strings[] array = {"大雄，男", "静香，女", "胖虎，男"};
        printName(
                //传入第一个逻辑
                s -> System.out.println("性别：" + s.split("，")[1] + ","),
                //传入第二个逻辑
                s -> System.out.println("姓名：" + s.split("，")[0] + "。"),
                //传入数据源
                array);
    }

    private static void consumerString(Consumer<Strings> one, Consumer<Strings> two) {
        //如果一个方法的参数和返回值全都是 Consumer 类型，那么就可以实现效果:消费数据的时候，
        // 首先做一个操作， 然后再做一个操作，实现组合。
        // 而这个方法就是 Consumer 接口中的default方法 andThen
        //先保存 one 的函数逻辑
        //再保存 two 的函数逻辑
        //最后 串行执行
        Consumer<Strings> stringConsumer = one.andThen(two);
        stringConsumer.accept("two");
    }

    public static void printName(Consumer<Strings> sex, Consumer<Strings> name, Strings[] array) {
        for (Strings s : array) {
            //先第一个再第二个
            sex.andThen(name).accept(s);
        }
    }


}
 
