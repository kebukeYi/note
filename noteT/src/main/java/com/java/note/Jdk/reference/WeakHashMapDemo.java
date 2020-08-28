package com.java.note.Jdk.reference;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @Author : mmy
 * @Creat Time : 2020/7/27  11:26
 * @Description
 */
public class WeakHashMapDemo {


    public static void getHashMap() {
        Map<String, Object> map = new HashMap<>();
        Integer integer = new Integer(1);
        String key = "HashMap";
        map.put(key, integer);
        System.out.println(map);
        key = null;
        System.out.println(map);
        System.gc();
        System.out.println(map + "\t " + map.size());

    }

    public static void getWeakHashMap() {
        WeakHashMap<String, Object> map = new WeakHashMap<>();
        Integer integer = new Integer(2);
        String key = "WeakHashMap";
        map.put(key, integer);
        System.out.println(map);

        // 这意味着"弱键"key再没有被其它对象引用，调用gc时会回收WeakHashMap中与key对应的键值对
        key = null;
        System.out.println(map);

        // 内存回收，这里会回收WeakHashMap中与"key"对应的键值对
        System.gc();
        System.out.println(map + "\t " + map.size());

    }

    public static void main(String[] args) {
        getWeakHashMap();
    }
}
