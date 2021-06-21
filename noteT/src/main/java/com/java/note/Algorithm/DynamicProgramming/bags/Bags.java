package com.java.note.Algorithm.DynamicProgramming.bags;

/**
 * @Author : mmy
 * @Creat Time : 2020/9/14  下午 9:34
 * @Description 01背包问题
 * wight  1 3 5 4
 * val      1 4 5 7
 */
public class Bags {

    /**
     * f[i][j]=max[f[i-1][j],f[i-1][j-c[i]]+w[i]]
     * 包括： 选第i个 和 不选第i个；
     */

    public static void main(Strings[] args) {
        int[] weight = new int[]{0, 1, 3, 4, 5};
        int[] values = new int[]{0, 1, 4, 5, 7};
        int sum = 7;
        System.out.println(get(weight, values, 7));
    }

    public static int get(int[] weight, int[] value, int sum) {
        int[][] f = new int[1024][];
        int n = weight.length - 1;
        int m = value.length - 1;

        for (int i = 1; i <= n; i++) {
            for (int j = 0; j <= m; j++) {
                f[i][j] = f[i - 1][j];
                if (j >= value[i])
                    f[i][j] = Math.max(f[i][j], f[i - 1][j - value[i]] + weight[i]);
            }
        }

        int res = 0;

        for (int i = 0; i <= m; i++) {
            res = Math.max(res, f[n][i]);
        }

        return res;
    }


}


