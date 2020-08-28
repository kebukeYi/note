package com.java.note.jvm;

/**
 * @Author : mmy
 * @Creat Time : 2020/7/26  16:06
 * @Description
 */
public class JvmDemo {

    static class ff {

    }


    //类的属性：常量、变量、成员属性
    private Object object = new Object();
    private static int i = 1;
    private static final String string = "hello";
    private static String string1 = "world";


    /**
     * 0 iconst_1
     * 1 istore_1  存储局部变量1
     * 2 sipush 128
     * 5 istore_2
     * 6 bipush -2   bipush: 把一个8位带符号的整数压入栈  7：对应的是 -2
     * 8 istore_3
     * 9 iload_1
     * 10 iload_2
     * 11 iadd
     * 12 iload_3
     * 13 imul   整数乘法
     * 14 ireturn
     */
    public static int add() {
        int a = 1;
        int b = 128;
        int c = -2;
        return (a + b) * c;
    }

    /**
     * 0 new #2 <com/java/note/jvm/JvmDemo>
     * 3 dup
     * 4 invokespecial #3 <com/java/note/jvm/JvmDemo.<init>>
     * 7 astore_1
     * 8 getstatic #4 <java/lang/System.out>
     * 11 aload_1
     * 12 invokevirtual #5 <com/java/note/jvm/JvmDemo.add>
     * 15 invokevirtual #6 <java/io/PrintStream.println>
     * 18 return
     */
    public static void main(String[] args) {
        //这两个 jvmDemo 在堆中 会指向 元空间中同一份 jvmDemo.class 结构
        JvmDemo jvmDemo = new JvmDemo();
        JvmDemo jvmDemo_1 = new JvmDemo();
        //不允许使用this 代表当前对象 访问静态代码
//        int add = this.add();
        int add = jvmDemo.add();
        System.out.println(add);
    }
}
