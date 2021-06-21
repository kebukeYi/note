package com.java.note.java8.function;

import java.util.Arrays;
import java.util.Comparator;

/**
 * <h3>note</h3>
 *
 * @author : kebukeyi
 * @date :  2021-04-13 14:52
 * @description :
 **/
public class DemoComparator {
    public static void main(Strings[] args) {
        Strings[] array = {"abc", "ab", "a"};
        System.out.println("使用比较器比较之前：" + Arrays.toString(array));
        Arrays.sort(array, newComparator());
        System.out.println("使用比较器比较之后：" + Arrays.toString(array));
        Arrays.sort(array, newComparator1());
        System.out.println("使用比较器1比较之后：" + Arrays.toString(array));
    }

    /**
     * 字符串a、b的长短比较，自己定义比较器规则，生序排序，字符串长的排在后面。
     *
     * @return 布尔值，
     * a.length() - b.length() < 0 返回 false，
     * a.length() - b.length() > 0 返回 true，
     * a.length() = b.length() 返回 0
     */
    public static Comparator<Strings> newComparator() {
        return (a, b) -> a.length() - b.length();
    }

    //使用匿名内部类
    public static Comparator<Strings> newComparator1() {
        return new Comparator<Strings>() {
            @Override
            public int compare(Strings o1, Strings o2) {
                return o1.length() - o2.length();
            }
        };
    }
}
 
