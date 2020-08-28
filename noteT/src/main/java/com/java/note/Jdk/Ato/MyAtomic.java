package com.java.note.Jdk.Ato;

import com.java.note.redis.bean.User;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/9  10:41
 * @Description
 */
public class MyAtomic {

    static AtomicReference<Integer> reference = new AtomicReference<>(100);
    static AtomicStampedReference<Integer> stampedReference = new AtomicStampedReference<>(100, 1);


    public static void main(String[] args) {

        new Thread(() -> {
            reference.compareAndSet(100, 101);
            reference.compareAndSet(101, 100);
        }, "t1").start();

        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            System.out.println(reference.compareAndSet(100, 2019) + "\t" + reference.get());
        }, "t2").start();

        System.out.println("****************************************************************************");

        new Thread(() -> {
            try {
                int stamp = stampedReference.getStamp();
                System.out.println("t3的第一次版本号  " + stamp);
                Thread.sleep(1000);
                stampedReference.compareAndSet(100, 101, stamp, stamp + 1);
                System.out.println("t3的第二次版本号  " + stampedReference.getStamp());
                stampedReference.compareAndSet(101, 100, stampedReference.getStamp(), stampedReference.getStamp() + 1);
                System.out.println("t3的第三次版本号  " + stampedReference.getStamp());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t3").start();

        new Thread(() -> {
            try {
                int stamp = stampedReference.getStamp();
                System.out.println("t4的第一次版本号  " + stamp);
                Thread.sleep(3000);
                boolean b = stampedReference.compareAndSet(100, 2019, stamp, stamp + 1);
                System.out.println(b + "\t" + stampedReference.getStamp());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t4").start();


    }

    //利用版本号 来完成ABA 问题
    public static void ABADemo() {

    }


    public static void atomicDemo() {
        Object o = new Object();

        AtomicInteger atomicInteger = new AtomicInteger();

        User user = new User("1", "pll");
        User user_1 = new User("2", "ho");
        AtomicReference<User> reference = new AtomicReference<>();
        reference.set(user);

        System.out.println(reference.compareAndSet(user, user_1) + "\t" + reference.get().toString());
        System.out.println(reference.compareAndSet(user_1, user) + "\t" + reference.get().toString());
        System.out.println(reference.compareAndSet(user, user_1) + "\t" + reference.get().toString());
    }
}
