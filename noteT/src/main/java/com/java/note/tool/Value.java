package com.java.note.tool;

/**
 * @Author : mmy
 * @Creat Time : 2020/4/1  13:09
 * @Description
 */
public class Value {
    public int i = 15;
}

class Test {
    public static void main(String argv[]) {
//        Test1 t = new Test1();
//        t.first();
    }

    public void first() {
        int i = 5;
        Value v = new Value();
        v.i = 25;
        second(v, i);
        System.out.println(v.i);
    }

    public void second(Value v, int i) {
        i = 0;
        v.i = 200;
        Value val = new Value();
        v = val;
        System.out.println(v.i + " " + i);
    }
}