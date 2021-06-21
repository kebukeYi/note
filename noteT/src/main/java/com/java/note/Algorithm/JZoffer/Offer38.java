package com.java.note.Algorithm.JZoffer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author : mmy
 * @Creat Time : 2020/9/11  上午 8:16
 * @Description 输入一个字符串，按字典序打印出该字符串中字符的所有排列。例如输入字符串 abc，
 * 则打印出由字符 a, b, c 所能排列出来的所有字符串 abc, acb, bac, bca, cab 和 cba。注意字典顺序;
 */
public class Offer38 {

    private Set<Strings> set = new HashSet<>(); // 去重
    private ArrayList<Strings> ret = new ArrayList<>(); // 返回

    public Strings[] Permutation(Strings str) {
        if (str == null) return new Strings[]{};
        boolean[] visited = new boolean[str.length()];
        dfs(str, "", visited);
        return set.toArray(new Strings[ret.size()]);
    }


    public void dfs(Strings str, Strings ch, boolean[] v) {
        if (str.length() == ch.length()) {
            set.add(ch);
            return;
        }

        for (int i = 0; i < str.length(); i++) {
            char temp = ch.charAt(i);
            if (v[i]) continue;
            v[i] = true;
            dfs(str, ch + temp + "", v);
            v[i] = false;
        }

        return;
    }


}
