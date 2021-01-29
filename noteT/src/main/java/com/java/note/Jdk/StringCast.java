package com.java.note.Jdk;

/**
 * @Author : mmy
 * @Creat Time : 2020/7/13  22:55
 * @Description
 */
public class StringCast {

    public static void main(String[] args) {
        /**
         * JVM 首先会检查该对象是否在字符串常量池中，如果在，就返回该对象引用，否则新的字符串将在常量池中被创建。
         * 这种方式可以减少同一个值的字符串对象的重复创建，节约内存。
         */

//        String str1 = "abc";
//        String str4 = "个";
//        String str5 = "积分";
//        String str6 = str1 + str4 + str5;

        /**
         *String str = new String(“abc”) 这种方式，首先在编译类文件时，"abc"常量字符串将会放入到常量结构中，在类加载时，
         * “abc"将会在常量池中创建；其次，在调用 new 时，JVM 命令将会调用 String 的构造函数，同时引用常量池中的"abc” 字符串，
         * 在堆内存中创建一个 String 对象；最后，str 将引用 String 对象。
         */
        //String str2 = new String("abc");

        /**
         * 如果常量池中有相同值，就会重复使用该对象，返回对象引用，这样一开始的对象就可以被回收掉。
         */
//        String str3 = str2.intern();
//
//        System.out.println(str1 == str2);//false
//        System.out.println(str2 == str3);//false
//        System.out.println(str1 == str3);//true
//
//        int i = 1;
//        int a = i++;
//        i = a;
//        System.out.println(i);
//        System.out.println(a);
        // System.out.println("===============================================");
//        0 iconst_2		常量2入栈
//        1 anewarray #2 <java/lang/String>		2出栈，新建一个数组并引用入栈
//        4 astore_1		将栈顶数组引用赋值给局部变量表1 = h 并弹出
//        5 aload_1		将局部变量表1=h 入栈
//        6 astore_2		将栈顶元素h引用赋值给局部变量表2 = g 并弹出
//        7 aload_1		将局部变量表1=h 入栈
//        8 iconst_0		常量0入栈
//        9 ldc #3 <ff>	将int, float或String型常量值从常量池中推送至栈顶	将字符串常量“ff”压入栈顶
//        11 aastore	将栈顶引用型值 存入 数组的指定索引位置 数组引用、索引、值依次弹出	数组指定索引存入 “ff” 全部弹出
//        12 aload_2		将局部变量表2=g 入栈
//        13 iconst_1		常量1入栈
//        14 iconst_1		常量1入栈
//        15 invokestatic #4 <java/lang/Integer.valueOf>	获取栈帧类型 并且要满足入参 随后创建新帧入栈	执行静态方法java/lang/Integer.valueOf 弹出栈顶元素 1 并入栈新 1
//        18 aastore	将栈顶引用型值 存入 数组的指定索引位置 数组引用、索引、值依次弹出	数组g 存入 1 全部弹出
//        19 return		返回
        String[] h = new String[2];
        Object[] g = h;
        h[0] = "ff";
        g[1] = new Integer(2);

    }
}
