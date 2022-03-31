package com.java.note.Jdk.thread.synchronizedd.black;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;

/**
 * @ClassName Main
 * @Author kebukeyi
 * @Date 2022/3/29 23:44
 * @Description 目的： 假如子线程没有获得锁的话 主线程不返回 继续阻塞
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
