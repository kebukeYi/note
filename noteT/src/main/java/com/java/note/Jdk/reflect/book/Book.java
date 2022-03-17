package com.java.note.Jdk.reflect.book;

/**
 * @author : kebukeYi
 * @date :  2022-03-17 11:11
 * @description :  测试 利用反射获得 final static 修饰符修饰的方法
 * @question :
 * @link :
 **/
public class Book {

    private Integer id;
    private String name;
    public Integer age;

    public Book() {
        System.out.println("Public 无参构造函数");
    }

    public Book(String name) {
        System.out.println("Public 带参构造函数");
    }

    private Book(String name, Double price) {
        System.out.println("Private 带两参构造函数");
    }

    public void printAll() {
        System.out.println("公开 打印 方法");
    }

    private void printOne() {
        System.out.println("私有  打印  方法");
    }

    public final void publicFinalPrint() {
        System.out.println("公开 final  打印  方法");
    }

    private final void privateFinalPrint() {
        System.out.println("私有 final  打印  方法");
    }

    public static void publicStaticPrint() {
        System.out.println("公开 Static  打印  方法");
    }

    private static void privateStaticPrint() {
        System.out.println("私有 Static  打印  方法");
    }

}
 
