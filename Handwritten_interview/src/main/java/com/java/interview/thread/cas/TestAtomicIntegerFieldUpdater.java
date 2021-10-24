package com.java.interview.thread.cas;

import com.java.interview.thread.consumer_provider.Print;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * @author : kebukeYi
 * @date :  2021-10-23 17:45
 * @description:
 * @question:
 * @link:
 **/
public class TestAtomicIntegerFieldUpdater {

    static class User {
        public volatile int age;
        public String name;

        public User(int age, String name) {
            this.age = age;
            this.name = name;
        }
    }

    public static void testAtomicIntegerFieldUpdater() {
        //使用静态方法 newUpdater( )创建一个更新器 updater
        AtomicIntegerFieldUpdater<User> updater = AtomicIntegerFieldUpdater.newUpdater(User.class, "age");
        User user = new User(1, "张三");
        //使用属性更新器的 getAndIncrement、getAndAdd 增加 user 的 age 值
        System.out.println(updater.getAndIncrement(user));// 1
        System.out.println(updater.getAndAdd(user, 100));// 101
        //使用属性更新器的 get 获取 user 的 age 值
        System.out.println(updater.get(user));// 101
    }

    public static void main(String[] args) {
        testAtomicIntegerFieldUpdater();
    }
}
 
