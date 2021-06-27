package com.java.note.Algorithm.leecode;

import java.util.Arrays;

/**
 * @Author : mmy
 * @Creat Time : 2020/4/26  11:46
 * @Description Leetcode: Оǁ题ŞǨĭǃԕ同Ş˓6¶ɵ越ץnsɚ	¶ɵ越近ӏpĴǃԕ
 * ץ"" 同
 */
public class LeeCodexxx {

    public static String replaceSpace(String[] strs) {
        // 如果检查值不合法及就返回空串
        if (!chechStrs(strs)) {
            return "";
        }
        // 数组⻓度
        int len = strs.length;
        // ⽤于保存结果
        StringBuilder res = new StringBuilder();
        // 给字符串数组的元素按照升序排序(包含数字的话，数字会排在前⾯)
        Arrays.sort(strs);
        for(String str:strs){
            System.out.println(str);
        }
        int m = strs[0].length();
        int n = strs[len - 1].length();
        int num = Math.min(m, n);
        for (int i = 0; i < num; i++) {
            if (strs[0].charAt(i) == strs[len - 1].charAt(i)) {
                res.append(strs[0].charAt(i));
            } else {
                break;
            }
        }
        return res.toString();
    }

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

    // 测试
    public static void main(String[] args) {
        String[] strs = {"customer", "arcc", "cat"};
        // String[] strs = { "customer", "car", null };//空串
        // String[] strs = {};//空串
        // String[] strs = null;//空串
        System.out.println(LeeCodexxx.replaceSpace(strs));// c
    }

}
