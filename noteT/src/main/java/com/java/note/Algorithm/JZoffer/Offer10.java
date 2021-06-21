package com.java.note.Algorithm.JZoffer;

import java.util.Arrays;

/**
 * @Author : mmy
 * @Creat Time : 2020/9/10  上午 8:51
 * @Description 斐波那契数列 题集
 */
public class Offer10 {

    public static void main(Strings[] args) {
//        System.out.println(fbnq(3));
        System.out.println(JumpFloorII(5));
        System.out.println(JumpFloorIII(5));
    }


    //考虑到第 i 项只与第 i-1 和第 i-2 项有关，因此只需要存储前两项的值就能求解第 i 项
    public static Integer fbnq(int n) {
        if (n <= 1)
            return n;

        int first = 0;
        int second = 1;
        int res = 0;
        for (int i = 2; i <= n; i++) {
            res = first + second;
            first = second;
            second = res;
        }

        return res;
    }

    //我们可以用 2*1 的小矩形 横着 或者 竖着 去覆盖更大的矩形。
    // 请问用 n 个 2*1 的小矩形无重叠地覆盖一个 2*n 的大矩形，总共有多少种方法？
    public static Integer get(int n) {
        if (n < 2) {
            return n;
        }
        int first = 1;
        int second = 2;
        int res = 0;
        for (int i = 3; i <= n; i++) {
            res = first + second;
            first = second;
            second = res;
        }
        return res;
    }

    //青蛙跳台阶
    public static Integer getTwo(int target) {
        if (target <= 2)
            return target;
        int pre1 = 1, pre2 = 2;
        int temp = 0;

        for (int i = 3; i <= target; i++) {
            temp = pre1 + pre2;
            pre1 = pre2;
            pre2 = temp;
        }
        return pre2;
    }

    //一只青蛙一次可以跳上 1 级台阶，也可以跳上 2 级… 它也可以跳上 n 级。求该青蛙跳上一个 n 级的台阶总共有多少种跳法。
    public static Integer JumpFloorII(int target) {
        int[] ints = new int[target];
        Arrays.fill(ints, 1);
        for (int i = 1; i < target; i++) {
            for (int j = 0; j < i; j++) {
                ints[i] = ints[i] + ints[j];
            }
        }
        return ints[target - 1];
    }

    // 2的 (n) 次方
    public static int JumpFloorIII(int target) {
        return target <= 0 ? 0 : 1 << (target);
    }


}

