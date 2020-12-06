package com.java.note.Algorithm.yyy.sort;

/**
 * @Author : mmy
 * @Creat Time : 2020/5/3  8:05
 * @Description
 */
public class QuickSort {


    public static void main(String[] args) {
        int[] array = {1, 200, 3, 4, 1, 0, -1, 84, 31, 65};
//        quickSort(array, 0, array.length - 1);
        quickTwo(array, 0, array.length - 1);

        for (int i : array) {
            System.out.println(i);
        }
    }

    /**
     * 分治算法
     * 时间：O(nlogn)  O(n^2)
     * 空间：O（n）
     */
    public static void quickSort(int[] array, int left, int right) {
        int i = left;
        int j = right;
        int mid = (i + j) / 2;
        //中间元素
        int privot = array[mid];

        while (i <= j) {

            while (privot > array[i]) {
                i++;
            }

            while (privot < array[j]) {
                j--;
            }

            if (i <= j) {
                int temp = array[j];
                array[j] = array[i];
                array[i] = temp;
                j--;
                i++;
            }
        }

        if (left < i) {
            quickSort(array, left, j);
        }

        if (i < right) {
            quickSort(array, i, right);
        }

    }


    public static void quickTwo(int[] array, int left, int right) {
        if (array == null || array.length == 0) return;
        if (left > right) return;

        int key = array[left];
        int l = left;
        int r = right;

        while (l != r) {
            //右侧指针需要左侧移动  寻找需要比 key 小的数值
            while (array[r] >= key && l < r) {
                r--;
            }

            //左侧指针需要右移  寻找需要比 key 大的数值
            while (array[l] <= key && l < r) {
                l++;
            }

            if (l < r) {
                int temp = array[l];
                array[l] = array[r];
                array[r] = temp;
            }
        }

        //这时 l==r  需要交换
        array[left] = array[l];
        array[l] = key;

        quickTwo(array, left, l - 1);
        quickTwo(array, l + 1, right);

    }

}
