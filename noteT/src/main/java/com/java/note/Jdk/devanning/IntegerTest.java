package com.java.note.Jdk.devanning;

/**
 * @author : kebukeYi
 * @date :  2021-09-26 12:46
 * @description:
 * @question:
 * @link:
 **/
public class IntegerTest {


    public static void main(String[] args) {
        //自动装箱
        int a = 11;
        //自动拆箱
        Integer a1 = 11;
        //大于缓存数组的值
        Integer b1 = 330;
        Integer c1 = 330;
        //手动建立对象
        final Integer integer = new Integer(11);

        System.out.println(a == a1);//true
        System.out.println(a == integer);//true
        System.out.println(b1 == c1);//false  自动装箱 生产了两个  new Integer(i); 对象
    }
}
 
