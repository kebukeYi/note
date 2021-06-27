package com.java.note.Algorithm.JZoffer;

/**
 * @Author : mmy
 * @Creat Time : 2020/9/10  下午 2:59
 * @Description 输入一个整数，输出该数二进制表示中 1 的个数。
 */
public class Offer15 {

    public static void main(String[] args) {
        System.out.println(get(7));
    }


    /**
     * 7  0111
     * 6  0110
     * 5  0101
     * 4  0100
     * 3 0011
     * 2 0010
     * n&(n-1)
     * 该位运算去除 n 的位级表示中最低的那一位。
     */
    public static int get(int n) {
        int count = 0;
        while (n != 0) {
            count++;
            n = n & (n - 1);
        }
        return count;
    }

    public int NumberOf1(int n) {
        return Integer.bitCount(n);
    }
}
