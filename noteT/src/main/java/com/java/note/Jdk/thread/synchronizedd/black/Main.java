package com.java.note.Jdk.thread.synchronizedd.black;

/**
 * @ClassName Main
 * @Author kebukeyi
 * @Date 2022/3/29 23:44
 * @Description
 * @Version 1.0.0
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("BEGIN");
        Object o = new Object();
        //主线程调用
        BlackHole.enter(o);
        System.out.println("END");
    }
}
