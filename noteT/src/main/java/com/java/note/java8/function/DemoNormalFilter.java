package com.java.note.java8.function;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author : kebukeyi
 * @date :  2021-04-13 16:36
 * @description :
 **/
public class DemoNormalFilter {

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        Collections.addAll(list, "Java", "C", "Python", "Hadoop", "Spark");

        System.out.print("筛选前的集合：");
        for (String s : list) {
            System.out.print(s + "，");
        }
        System.out.println();

        System.out.print("经过条件1筛选后的集合：");
        for (String s : list) {
            if (s.length() >= 4) {
                System.out.print(s + "，");
            }
        }
        System.out.println();

        System.out.print("经过条件2筛选后的集合：");
        for (String s : list) {
            if (s.length() >= 5) {
                System.out.print(s + "，");
            }
        }
        System.out.println();
    }
}
 
