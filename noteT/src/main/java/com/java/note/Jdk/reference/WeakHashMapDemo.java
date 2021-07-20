package com.java.note.Jdk.reference;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @Author : mmy
 * @Creat Time : 2020/7/27  11:26
 * @Description: https://baijiahao.baidu.com/s?id=1666368292461068600&wfr=spider&for=pc
 */
public class WeakHashMapDemo {


    public static void getHashMap() throws InterruptedException {
        Map<String, Object> map = new HashMap<>();
        //没有使用到 缓存
        Integer integer = new Integer(1);
        String key = "HashMap";
        map.put(key, integer);
        System.out.println("1  " + map);
        key = null;
        System.out.println("2  " + map);
        System.gc();
        Thread.sleep(1000);
        System.out.println("3  " + map + "\t " + map.size());

    }

    public static void getWeakHashMap() throws InterruptedException {
        WeakHashMap<Key, Object> map = new WeakHashMap<>();
        //没有使用到 缓存
        Integer integer = new Integer(2);
        Key key1 = new Key("hh");
        String key = "WeakHashMap";
        map.put(key1, integer);
        System.out.println("1  " + map);

        // 这意味着"弱键"key再没有被其它对象引用，调用gc时会回收WeakHashMap中与key对应的键值对
        key = null;  //失效 尴尬
        key1 = null;
        System.out.println("2  " + map);

        // 内存回收，这里会回收WeakHashMap中与"key"对应的键值对
        System.gc();
        Thread.sleep(1000);
        System.out.println("3  " + map + "\t " + map.size());
    }

    public static void main(String[] args) throws InterruptedException {
        getHashMap();
        System.out.println("=========================");
        getWeakHashMap();
    }

    @Data
    @AllArgsConstructor
    static
    class Key {
        String key;
    }
}
