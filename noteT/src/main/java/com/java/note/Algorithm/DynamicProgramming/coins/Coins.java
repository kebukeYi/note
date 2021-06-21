package com.java.note.Algorithm.DynamicProgramming.coins;

/**
 * @Author : mmy
 * @Creat Time : 2020/9/14  下午 9:07
 * @Description 27元 利用 2 5 7 元 获取硬币数最小数量
 */
public class Coins {


    /**
     * 设最后一枚硬币 可能是 2 5 7 然后再求各自剩下的子问题，就转换为了子问题求解
     * f(27)=min[f(27-2)+1,f(27-5)+1,f(27-7)+1]
     */

//    利用递归思想来实现
    static int res = Integer.MAX_VALUE;

    public static int f(int n) {
        if (n == 0) {
            return 0;
        }
        if (n >= 2) {
            res = Math.min(f(n - 2) + 1, res);
        }

        if (n >= 5) {
            res = Math.min(f(n - 5) + 1, res);
        }

        if (n >= 7) {
            res = Math.min(f(n - 7) + 1, res);
        }

        return res;
    }

    public static void main(Strings[] args) {
        int f = f(10);
        System.out.println(f);
    }

}
