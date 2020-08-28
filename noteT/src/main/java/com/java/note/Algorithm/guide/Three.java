package com.java.note.Algorithm.guide;

import java.util.HashSet;

/**
 * @Author : mmy
 * @Creat Time : 2020/4/29  21:21
 * @Description 构造最长回文串
 * 回文串:“回文串”是一个正读和反读都一样的字符串,比如 level"或者“noon"等等就是回文串
 */
public class Three {

    /**
    字符出现的次数为双数
    字符出现的次数为双数+1单个字符的字数
     */
    public static int creatLongStr(String s) {
        char[] chars = s.toCharArray();
        HashSet<Character> hashSet = new HashSet<>();
        int count = 0;
        for (char c : chars) {
            //如果存在就说明有双数
            if (hashSet.contains(c)) {
                //开始下一组
                hashSet.remove(c);
                count++;
            } else {
                hashSet.add(c);
            }
        }

        return hashSet.isEmpty() ? count * 2 : count * 2 + 1;
    }


    public static void main(String[] args) {
        System.out.println(creatLongStr("abccccdd"));
    }
}
