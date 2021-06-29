package com.java.note.Algorithm.yyy.sort;

import java.util.Arrays;

/**
 * @Author : mmy
 * @Creat Time : 2020/5/3  16:59
 * @Description
 */
public class ShellSort {

    public static void shellSorts(int[] array) {
        int gap = array.length / 2;
        //增量每次都/2
        for (int step = gap; step > 0; step /= 2) {
            //从增量那组开始进⾏ 向 插⼊排序，直⾄完毕
            for (int i = step; i < array.length; i++) {
                int j = i;
                int temp = array[j];
                while (j - step >= 0 && array[j - step] > temp) {
                    array[j] = array[j - step];
                    j = j - step;
                }
                array[j] = temp;
            }
        }
    }

    public static void shellSort(int[] array) {
        //增量
        int d = array.length;
        while (d > 1) {
            d = d >> 1;
            //组数
            for (int i = 0; i < d; i++) {
                //每一组用向右递增 增量d
                for (int j = i + d; j < array.length; j = j + d) {
                    int temp = array[j];
                    int x;
                    //每一组 从右向左 进行对比 插入
                    for (x = j - d; (x >= 0) && (array[x] > temp); x = x - d) {
                        array[x + d] = array[x];
                    }
                    array[x + d] = temp;
                }
            }
        }
    }

    public static void main(String[] args) {
        int[] arr = {9, 8, 7, 6, 5, 4, 3, 2, 1};
        shellSorts(arr);
        System.out.println(Arrays.toString(arr));
        arr = new int[]{9, 8, 7, 6, 5, 4, 3, 2, 1};
        shellSort(arr);
        System.out.println(Arrays.toString(arr));
    }


}
