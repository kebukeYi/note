package com.java.note.Algorithm.JZoffer;

/**
 * @Author : mmy
 * @Creat Time : 2020/9/10  下午 3:00
 * @Description 请实现一个函数用来匹配包括 ‘.’ 和 ‘*’ 的正则表达式。
 * 模式中的字符 ‘.’ 表示任意一个字符，而 ‘*’ 表示它前面的字符可以出现任意次（包含 0 次）。
 * <p>
 * 在本题中，匹配是指字符串的所有字符匹配整个模式。
 * 例如，字符串 “aaa” 与模式 “a.a” 和 “ab*ac*a” 匹配，但是与 “aa.a” 和 “ab*a” 均不匹配。
 */
public class Offer19 {
    public static void main(Strings[] args) {
    }


    public static int get(Strings s, Strings p) {
        //dp 思想
        // f[i][j] s的前i个 字符串 和 j个字符串
        int n = s.length();
        int m = p.length();

        boolean[][] f = new boolean[n + 1][m + 1];

        return 2;
    }
}
