package com.effective.dev.one;

import java.util.EnumSet;
import java.util.ServiceLoader;

/**
 * @author : kebukeyi
 * @date :  2021-04-14 11:49
 * @description : 静态工厂的第四大优势
 * 基本用法：https://www.cnblogs.com/wgl1995/p/9401652.html
 **/
public class StaticFactory {

    enum Season {
        SPRING, SUMMER, FALL, WINTER
    }

    public static void getEnumSet() {
        EnumSet<Season> seasonEnumSet = EnumSet.allOf(Season.class);
        System.out.println(seasonEnumSet);
        //静态工厂用法，会根据枚举类型的元素个数来选择不同的返回
        EnumSet<Season> eEnumSet = EnumSet.noneOf(Season.class);
        eEnumSet.add(Season.SPRING);
        System.out.println(eEnumSet);
        System.out.println(eEnumSet.getClass());
    }


    public static void getServiceLoader() {
        ServiceLoader<Season> load = ServiceLoader.load(Season.class, ClassLoader.getSystemClassLoader());
        System.out.println(load);
    }


    public static void main(String[] args) {
        getEnumSet();
        getServiceLoader();
    }


}
 
