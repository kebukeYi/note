package com.java.note.Jdk.reflect.book;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author : kebukeYi
 * @date :  2022-03-17 11:15
 * @description :
 * @question :
 * @link :
 **/
public class ReflectBook {

    public static Object getInstanceClassForName(String packageName) {
        Class<?> aClass = null;
        Object newInstance = null;
        try {
            aClass = Class.forName(packageName);
            newInstance = aClass.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return newInstance;
    }

    public static Object getInstanceByClass(Class c) {
        try {
            final Object newInstance = c.newInstance();
            return newInstance;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static Class getClassByClasspath(String classPath) {
        try {
            final Class<?> aClass = Class.forName(classPath);
            return aClass;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws IllegalAccessException, InstantiationException {
        String classPath = "com.java.note.Jdk.reflect.book.Book";
        final Class aClass = getClassByClasspath(classPath);
        final Object newInstance = aClass.newInstance();
        String methodName = "publicFinalPrint";
        try {
            final Method method = aClass.getMethod(methodName);
            //可以通过反射获得 final 修饰的方法
            method.invoke(newInstance, null);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
 
