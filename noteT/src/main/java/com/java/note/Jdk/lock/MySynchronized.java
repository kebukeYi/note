package com.java.note.Jdk.lock;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author : mmy
 * @Creat Time : 2020/9/19  上午 8:12
 * @Description
 */
public class MySynchronized {


    @Data
    @NoArgsConstructor
    static class Cast {
        public static Object lock = new Object();


    }


    public static synchronized void get() {
        System.out.println(Thread.currentThread().getName());
    }

    public static void h() {
        synchronized (Cast.lock) {

        }
    }

    public static void set() {
        synchronized (Cast.class) {
            System.out.println(Thread.currentThread().getName());
        }
    }

    public static void main(Strings[] args) {

    }


}
