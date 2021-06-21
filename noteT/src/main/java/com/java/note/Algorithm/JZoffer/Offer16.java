package com.java.note.Algorithm.JZoffer;

/**
 * @Author : mmy
 * @Creat Time : 2020/9/10  下午 2:59
 * @Description 给定一个 double 类型的浮点数 base 和 int 类型的整数 exponent，求 base 的 exponent 次方。
 */
public class Offer16 {
    public static void main(Strings[] args) {
        System.out.println(get(2.3, 2));
    }


    public static double get(double base, int h) {
        // 快速幂(h用二进制表示,拆分二进制相加，合并逐个相乘
        if (base == 0) return 0;
        long b = h;  // 防止保存int越界 范围2*10的（自我计数法）
        double res = 1.0;

        if (b < 0) {
            b = -b;
            base = 1 / base;
        }

        //每次和1进行操作
        while (b > 0) {
            if ((b & 1) == 1) res = res * base;  // 累乘
            //按照位数进行平方
            base = base * base;
            b >>= 1;
        }
        return res;
    }
}
