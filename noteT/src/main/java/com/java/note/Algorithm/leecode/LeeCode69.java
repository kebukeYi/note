package com.java.note.Algorithm.leecode;

/**
 * @Author : mmy
 * @Creat Time : 2020/4/21  21:46
 * @Description 平方根
 */
public class LeeCode69 {

    //二分查找法
    public static long computSqrt(long a) {
        if (a == 0 || a == 1) {
            return a;
        }
        long left = 1;
        long right = a / 2;
        long num;
        while (left <= right) {
            long mid = left + (right - left) / 2;
            num = mid * mid;
            if (num > a) {
                right = mid - 1;
            } else if (num < a) {
                left = mid + 1;
            } else {
                return mid;
            }
        }
        return right;
    }

    //牛顿迭代法
    public static float InvSqrt(float x) {
        float xhalf = x / 2;
        int i = (int) x;
        i = 0x5f3759df - (i >> 1);
        x = (float) i;
        x = x * (1.5f - xhalf * x * x);
        return x;
    }

    public static int mySqrt(int a) {
        long x = a;
        while (x * x > a) {
            x = (x + a / x) / 2;
        }
        return (int) x;
    }


    public static void main(String[] args) {
        System.out.println(computSqrt(8));
        System.out.println(InvSqrt(8));
        System.out.println(mySqrt(8));
    }

}
