package com.java.note.Algorithm.bytes;

import java.util.Arrays;

/**
 * @Author : mmy
 * @Creat Time : 2020/4/24  8:34
 * @Description 长度为N的数组ar, 一定可以组成N^2个数值对。
 * 例如arr=[3,1,1,4]
 * 数值对有(3,3)(3,1)(3,2)(3,4)    (1,3)(1,1)(1,2)(1,4)   (2,3)(2,1)(2,2),(2,4)   (4,3)(4,1)(4,2)(4,4)
 * 也就是任意两个数都有
 * 数值对,而且自己和自己也算数值对。
 * 数值对怎么排序?规定,第一维数据从小到大,第一维数据一样的,第二维
 * 数组也从小到大。所以上面的数值对排序的结果为
 * (1,1)(1,2)(1,3)(2,1)(2,2)(2,3)3,1)32)(33)
 * 给定一个数组ar,和整数k,返回第k小的数值对。
 */
public class Array2 {

    //第 k 个键值对
    public static int[] creatMin(int[] res, int k) {
        int length = res.length;
        int n = length * length;
        int group = (k - 1) / length;
        int index = (k - 1) % length;
        int x = res[group];
        int y = res[index];
        return new int[]{x, y};
    }


    //第 k 个小 键值对
    public static int[] creatArray2(int[] res, int k) {
        Arrays.sort(res);//O(N*logN)
        int group = res[(k - 1) / res.length];//下标
        int less = 0;
        int groupNum = 0;
        for (int i = 0; i < res.length; i++) {
            if (res[i] < group) {
                less++;
            } else if (res[i] == group) {
                groupNum++;
            }
        }

        int out = less * res.length;
        int rest = k - out;
        int index = (rest - 1) / groupNum;
        int y = res[index];

        return new int[]{group, y};
    }

    public int[] creatArrayThree(int[] res, int k) {
        return null;
    }

    //从无序数组中找到第K小的数字
    public int findMinNum(int[] res, int k) {
        return 1;
    }


    public static void main(Strings[] args) {
        int[] arr = {3, 1, 2, 4};
        int k = 4;
        System.out.println(creatMin(arr, k)[0]);
        System.out.println(creatMin(arr, k)[1]);
        System.out.println(creatArray2(arr, k)[1]);
        System.out.println(creatArray2(arr, k)[1]);


    }
}
