package com.java.note.Jdk.devanning;

/**
 * @author : kebukeYi
 * @date :  2021-09-26 12:58
 * @description:
 * @question:
 * @link:
 **/
public class StringBuilderTest {


    public static void main(String[] args) {
        ai();
        bi();
    }

    //一般的 + 号 拼接字符串 有什么缺点?
    public static void ai() {
        String name = "";
        //在普通的 for 循环中 进行拼接的话 会产生很多不必要的中间 字符串对象 造成内存泄露
        for (int i = 0; i < 10; i++) {
            name = name + i;
        }
        System.out.println(name);
    }

    public static void bi() {
        String name = "";
        //底层是一个 char[] 数组 , 每次 append() 都只是进行 copy() 操作 没有涉及到对像的产生
        StringBuilder builder = new StringBuilder(name);
        //在普通的 for 循环中 进行拼接的话 会产生很多不必要的中间 字符串对象 造成内存泄露
        for (int i = 0; i < 10; i++) {
            builder.append(i);
        }
        builder.reverse();
        System.out.println(builder.toString());
    }
}
 
