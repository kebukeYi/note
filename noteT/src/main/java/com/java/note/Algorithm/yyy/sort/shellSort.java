package com.java.note.Algorithm.yyy.sort;

/**
 * @Author : mmy
 * @Creat Time : 2020/5/3  16:59
 * @Description
 */
public class shellSort {

    public static void main(String[] args) {
        int[] arr = {9, 8, 7, 6, 5, 4, 3, 2, 1};
    }


    public static void shellSorts(int[] array) {
        int gap = array.length / 2;
        //增量每次都/2
        for (int step = gap; step > 0; step /= 2) {
            //从增量那组开始进⾏插⼊排序，直⾄完毕
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
}
