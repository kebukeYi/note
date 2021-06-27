package com.java.note.Jdk.utils.hash;

import java.util.HashMap;

/**
 * @Author : mmy
 * @Creat Time : 2020/4/6  10:35
 * @Description
 */
public class HashUtils {

    public static void getString() {
        //常量池中 新建 abc
        String str1 = "abc";
        String str2 = new String("abc");
        String str3 = "abc";

        String str4 = "xxx";
        String str5 = "abc" + "xxx";
        String str6 = str3 + str4;

        System.out.println(str1 == str2);
        System.out.println(str1.hashCode() + "\t" + str2.hashCode());

        System.out.println(str5 == str6);
        System.out.println(str5.hashCode() + "\t" + str6.hashCode());

        System.out.println("======================");

        System.out.println(str1 == str2.intern());
        System.out.println(str1.hashCode() + "\t" + str2.hashCode());

        System.out.println(str1 == str3.intern());

        System.out.println(str5 == str6.intern());
        System.out.println(str5.hashCode() + "\t" + str6.hashCode());


    }

    public static void getIneteger() {
    }


    public static void getHash() {
        String String = new String("123");
        String str = new String("123");

        System.out.println(str == String);
        System.out.println(str.hashCode() + "\t" + String.hashCode());
    }

    public static void getHashMap() {
        HashMap<String, Double> map = new HashMap(16);
        map.put("K1", 1.0);
        map.put("K2", 2.0);
        map.put("K3", 3.0);
        map.put("K4", 4.0);
        map.put("K5", 5.0);
        map.put("K6", 6.0);
        map.put("K7", 7.0);
        map.put("K8", 8.0);
        map.put("K9", 9.0);
        map.put("K10", 10.0);

        map.remove("K10");
        map.remove("K9");
        map.remove("K8");
        map.remove("K7");
        map.remove("K6");
    }


    public static void main(String[] args) {
        getString();

    }
}
