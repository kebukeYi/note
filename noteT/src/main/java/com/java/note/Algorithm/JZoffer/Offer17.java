package com.java.note.Algorithm.JZoffer;

/**
 * @Author : mmy
 * @Creat Time : 2020/9/10  下午 2:59
 * @Description 输入数字 n，按顺序打印出从 1 到最大的 n 位十进制数。比如输入 3，则打印出 1、2、3 一直到最大的 3 位数 999。
 * <p>
 * 示例 1:
 * <p>
 * 输入: n = 1
 * 输出: [1,2,3,4,5,6,7,8,9]
 */
public class Offer17 {
    public static void main(String[] args) {
        System.out.println(get(2));
    }

    //不考虑溢出
    public static int[] get(int h) {

        int x = 10;
        int res = 1;
        //快速幂
        while (h != 0) {
            if ((h & 1) == 1) res = res * x;
            x *= x;
            h >>= 1;
        }

        //首先可以快速获得  10000 然后 再执行减一 获得4位数的 9999
        int length = res - 1;
        int[] attays = new int[length];
        for (int i = 0; i < length; i++) {
            attays[i] = i + 1;
        }
        return attays;
    }
}
