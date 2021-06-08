package com.java.note.jvm;

/**
 * @author : kebukeyi
 * @date :  2021-05-31 08:52
 * @description :
 * @question :
 * @usinglink :
 **/
public class StaticTest {

    private final int aa = 123;
    private final String ss = "123";

    //static 时期  new 关键字根本没有完成
    private static final String bb = new StaticTest().ss;
    private static final int cc = new StaticTest().aa;

    public StaticTest() {
        System.out.println("bb =" + bb);
        System.out.println("cc =" + cc);
        System.out.println("--------------");
    }

    public static void main(String[] args) {
        new StaticTest();
    }
}
