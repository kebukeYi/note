package com.java.note.Algorithm.yyy.sort;

import java.util.Arrays;

/**
 * @Author : mmy
 * @Creat Time : 2020/5/3  17:38
 * @Description
 */
public class InsertSort {



    public static void main(String[] args) {
        int[] arr = {9, 8, 7, 6, 5, 4, 3, 2, 1};
        insertSort(arr);
        System.out.println(Arrays.toString(arr));
    }


    /**
     * 时间：O(n^2)
     * 空间：O（1）
     */
    public static void insertSort(int[] array) {
        for (int i = 1; i < array.length; i++) {
            int temp = array[i];
            //如果前⼀位(已排序的数据)⽐当前数据要⼤，那么就进⼊循环⽐较[参考第⼆趟排序]
            int j = i - 1;
            while (j >= 0 && array[j] > temp) {
                //往后退⼀个位置，让当前数据与之前前位进⾏⽐较
                array[j + 1] = array[j];
                j--;
            }
            //退出了循环说明找到了合适的位置了，将当前数据插⼊合适的位置中
            array[j + 1] = temp;
        }

    }
}
