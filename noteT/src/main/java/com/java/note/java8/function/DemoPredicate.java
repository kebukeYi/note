package com.java.note.java8.function;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author : kebukeyi
 * @date :  2021-04-13 15:18
 * @description :
 **/
public class DemoPredicate {

    public static void main(Strings[] args) {
        Strings[] array = {"迪丽热巴,女", "古力娜扎,女", "马尔扎哈,男", "赵丽颖,女"};
        List<Strings> list = filter(
                array,
                s -> "女".equals(s.split(",")[1]),
                s -> s.split(",")[0].length() == 4
        );
        System.out.println(list);
    }

    private static List<Strings> filter(Strings[] array, Predicate<Strings> one, Predicate<Strings> two) {
        List<Strings> list = new ArrayList<>();
        for (Strings info : array) {
            if (one.and(two).test(info)) {
                list.add(info);
            }
        }
        return list;
    }
}
 
