package com.java.note.Algorithm.mt;

/**
 * @Author : mmy
 * @Creat Time : 2020/4/23  21:25
 * @Description 输入一个int类型的值N, 构造一个长度为N的数组a并返回
 * 要求
 * 对任意的<k≤j,都满足ar[+ari!=ark]*2
 * <p>
 * [3,1,2]  设一个数组都达标  那么把组中的每个元素都进行改变也都满足
 * 奇数变换：把每个元素改为 第i个奇数  N个
 * 偶数变换：把每个元素改为 第i个偶数  N个
 * 最后拼接 2N个 个数的元素
 * 比如 生成长度为7的数组  ：先生成4个元素的
 */
public class Array {

    public static int[] createArray(int n) {
        if (n == 0) {
            return null;
        } else if (n == 1) {
            return new int[]{1};
        }
        //一半长达标   7：4 个
        int halfSize = (n + 1) / 2;
        //递归生成各一半数组
        int[] base = createArray(halfSize);
        //等长奇数达标来
        //等长偶数达标来
        int[] res = new int[n];
        int index = 0;
        for (; index < halfSize; index++) {
            res[index] = base[index] * 2 + 1;
        }
        for (int i = 0; index < n; index++, i++) {
            res[index] = base[i] * 2;
        }
        return res;
    }

    public static boolean isVaild() {
        createArray(1024);
        return true;
    }

    public static void main(String[] args) {

    }
}
