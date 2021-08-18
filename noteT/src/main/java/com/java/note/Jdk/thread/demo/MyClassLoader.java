package com.java.note.Jdk.thread.demo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Author : mmy
 * @Creat Time : 2020/4/26  10:08
 * @Description ；
 * 1.为什么要自定义ClassLoader
 * 因为系统的ClassLoader只会加载指定目录下的class文件,如果你想加载自己的class文件,那么就可以自定义一个ClassLoader
 * 2.如何自定义ClassLoader
 * 2.1
 * 新建一个类继承自java.lang.ClassLoader,重写它的findClass方法。
 * 2.2
 * 将class字节码数组转换为Class类的实例
 * 2.3
 * 调用loadClass方法即可
 */
public class MyClassLoader extends ClassLoader {

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Class log = null;
        // 获取该class文件字节码数组
        byte[] classData = getData();
        if (classData != null) {
            // 将class的字节码数组转换成Class类的实例
            log = defineClass(name, classData, 0, classData.length);
        }
        return log;
    }

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        MyClassLoader myClassLoader = new MyClassLoader();
        //查找Log这个class文件
        myClassLoader.findClass("Log");
        //加载Log这个class文件
        Class<?> Log = myClassLoader.loadClass("Log");

        //类加载器是:com.java.note.thread.demo.MyClassLoader@3a71f4dd
        System.out.println("类加载器是:" + Log.getClassLoader());

        //利用反射获取main方法
        Method method = Log.getDeclaredMethod("main", String[].class);
        Object object = Log.newInstance();
        String[] arg = {"ad"};
        method.invoke(object, (Object) arg);
    }

    private byte[] getData() {
        //指定路径
        String path = "E:\\TooL\\Log.class";
        File file = new File(path);
        FileInputStream in = null;
        ByteArrayOutputStream out = null;
        try {
            in = new FileInputStream(file);
            out = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int size = 0;
            while ((size = in.read(buffer)) != -1) {
                out.write(buffer, 0, size);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
        return out.toByteArray();
    }
}
