package com.java.interview.thread.cas;

import java.lang.reflect.Field;

/**
 * @author : kebukeYi
 * @date :  2021-10-25 09:11
 * @description:
 * @question:
 * @link: https://www.toutiao.com/i6805578326279717390?wid=1635124208794
 **/
public class Demo {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        Class cache = Integer.class.getDeclaredClasses()[0];
        Field c = cache.getDeclaredField("cache");
        c.setAccessible(true);
        Integer[] array = (Integer[]) c.get(cache);
        // array[129] is 1
        array[130] = array[129];
        // Set 2 to be 1
        array[131] = array[129];
        // Set 3 to be 1
        Integer a = 1;
        //相当于 把原本放2的位置改为了1 把放3的位置改为了1 然后去比较
        if (a == (Integer) 1 && a == (Integer) 2 && a == (Integer) 3) {
            System.out.println("Success");
        }
    }
}
 
