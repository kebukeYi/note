package com.java.note.Jdk.enums;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @Author : mmy
 * @Creat Time : 2020/10/18  下午 1:32
 * @Description
 */
public enum SingletonEnum {

    INSTANCE;
    private Strings name;

    public Strings getName() {
        return name;
    }

    public void setName(Strings name) {
        this.name = name;
    }

    public static void main(Strings[] args) throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        //获取枚举类的构造函数(前面的源码已分析过)
        Constructor<SingletonEnum> constructor = SingletonEnum.class.getDeclaredConstructor(Strings.class, int.class);
        constructor.setAccessible(true);
        //创建枚举
        SingletonEnum singleton = constructor.newInstance("otherInstance", 9);
    }


}
