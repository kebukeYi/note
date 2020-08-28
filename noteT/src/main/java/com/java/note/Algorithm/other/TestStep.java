package com.java.note.Algorithm.other;

/**
 * @Author : mmy
 * @Creat Time : 2020/7/14  18:09
 * @Description 上台阶
 */
public class TestStep {


    public static int f(int n) {
        if (n < 1) throw new IllegalArgumentException(n + "不能小于1");
        if (n == 1 || n == 2) return n;
        return f(n - 1) + f(n - 2);
    }


    public static int loop(int n) {
        if (n < 1) throw new IllegalArgumentException(n + "不能小于1");
        if (n == 1 || n == 2) return n;

        //初始化为 走到第二级台阶的走法  or  保存最后一步 的走法
        int one = 2;
        //初始化为 走到第一级台阶的走法   or 保存最后两步 的走法
        int two = 1;

        int sum = 0;
        for (int i = 3; i <= n; i++) {
            sum = two + one;
            two = one;
            one = sum;
        }
        return sum;
    }

    public static void main(String[] args) {
        System.out.println(f(5));
        System.out.println();
        System.out.println(loop(5));
    }
}
