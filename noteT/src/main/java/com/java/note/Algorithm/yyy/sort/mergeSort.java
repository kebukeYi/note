package com.java.note.Algorithm.yyy.sort;

import java.util.Arrays;

/**
 * @Author : mmy
 * @Creat Time : 2020/5/3  8:38
 * @Description 归并算法 分治思想
 */
public class mergeSort {

    public static void main(String[] args) {
        int[] arr = {9, 8, 7, 6, 5, 4, 3, 2, 1};
//        int[] arr = {9, 8, 7};
        sort(arr, 0, arr.length);
        System.out.println(Arrays.toString(arr));
    }

    /**
     * 时间：O(logN)
     * 空间：O（N）
     */
    public static void sort(int[] array, int left, int right) {
        //如果只有⼀个元素，那就不⽤排序了
        if (left == right) {
            return;
        }
        int mid = (left + right) / 2;
        sort(array, left, mid);
        sort(array, mid + 1, right);
        merge(array, left, mid + 1, right);
    }

    public static void merge(int[] array, int left, int mid, int right) {
        int[] leftArray = new int[mid - left];
        int[] rightArray = new int[right - mid + 1];

        for (int i = left; i < mid; i++) {
            leftArray[i - left] = array[i];
        }

        for (int i = mid; i <= right; i++) {
            rightArray[i - mid] = array[i];
        }

        int i = 0;
        int j = 0;
        int k = left;

        while (i < leftArray.length && j < rightArray.length) {
            if (leftArray[i] < rightArray[j]) {
                array[k] = leftArray[i];
                k++;
                i++;
            } else {
                array[k] = rightArray[j];
                k++;
                j++;
            }
        }

        while (i < leftArray.length) {
            array[k] = leftArray[i];
            i++;
            k++;
        }

        while (j < rightArray.length) {
            array[k] = leftArray[j];
            j++;
            k++;
        }

    }
}
