package com.java.note.Jdk.model.single;

/**
 * @Author : mmy
 * @Creat Time : 2020/7/14  16:36
 * @Description 静态内部类；
 * 在静态内部类被加载和初始化时，才创建Singleton6 实例对象；
 * 静态内部类是不会自动随着外部类的加载和初始化而初始化，它是要单独去加载和初始化；
 * 因为在内部类加载的，因此是线程安全的；
 */
public class Singleton6 {

    private Singleton6() {

    }

    private static class Inner {
        private static final Singleton6 SINGLETON6 = new Singleton6();
    }

    public static Singleton6 getInstance() {
        return Inner.SINGLETON6;
    }
}
