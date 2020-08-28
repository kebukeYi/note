package com.java.note.Jdk.reference;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author : mmy
 * @Creat Time : 2020/7/27  11:18
 * @Description 软应用 弱引用 实战
 * 假如有一个应用需要读取大量的本地图片
 * 如果每次读取图片都从硬盘读取则会严重影响性能,
 * 如果一次性全部加载到内存中又可能造成内存溢出
 * 此时使用软引用可以解决这个问题
 */
public class SoftReferenceWork {
    /**
     * 设计思路是:用一个 Hash Map来保存图片的路径和相应图片对象关联的软引用之间的映射关系,在内存不足时
     * JVM会自动回收这些缓存图片对象所占用的空间,从而有效地避免了OOM的问题;
     */

    public static void getImagesData() {
        Map<String, SoftReference<Object>> stringSoftReferenceMap = new HashMap<>();
    }

    public static void main(String[] args) {

    }
}
