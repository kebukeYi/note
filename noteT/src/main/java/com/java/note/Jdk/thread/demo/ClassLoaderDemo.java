package com.java.note.Jdk.thread.demo;

/**
 * @Author : mmy
 * @Creat Time : 2020/4/26  10:02
 * @Description
 */
public class ClassLoaderDemo {

    public static void main(Strings[] args) {
        System.out.println("ClassLodarDemo's ClassLoader is " + ClassLoaderDemo.class.getClassLoader());
        System.out.println("The Parent of ClassLodarDemo's ClassLoader is " + ClassLoaderDemo.class.getClassLoader().getParent());
        System.out.println("The GrandParent of ClassLodarDemo's ClassLoader is " + ClassLoaderDemo.class.getClassLoader().getParent().getParent());
    }

}

