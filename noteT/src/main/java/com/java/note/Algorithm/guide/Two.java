package com.java.note.Algorithm.guide;

import java.util.Arrays;

/**
 * @Author : mmy
 * @Creat Time : 2020/4/29  21:07
 * @Description 最长公共前缀
 * 排序后 把第一个和最后一个拿来作比较
 */
public class Two {

    private static boolean chechStrs(String[] strs) {
        boolean flag = false;
        if (strs != null) {
            // 遍历strs检查元素值
            for (int i = 0; i < strs.length; i++) {
                if (strs[i] != null && strs[i].length() != 0) {
                    flag = true;
                } else {
                    flag = false;
                    break;
                }
            }
        }
        return flag;
    }

    public static String findLongPreStr(String[] strings) {
        if (!chechStrs(strings)) return "";
        Arrays.sort(strings);
        int m = strings[0].length();
        int n = strings[strings.length - 1].length();
        String a = strings[0];
        String b = strings[strings.length - 1];
        int num = Math.min(m, n);
        int i = 0;
        for (; i < num; i++) {
            if (a.charAt(i) == b.charAt(i)) {
                continue;
            } else {
                break;
            }
        }
        String result = a.substring(0, i);
        return result;

    }

    public static void main(String[] args) {
//        String[] strs = { "customer", "car", "cat" };
        String[] strs = {"flower", "flow", "flight"};
        // String[] strs = { "customer", "car", null };//空串
        // String[] strs = {};//空串
        // String[] strs = null;//空串
        System.out.println(findLongPreStr(strs));
    }
}
