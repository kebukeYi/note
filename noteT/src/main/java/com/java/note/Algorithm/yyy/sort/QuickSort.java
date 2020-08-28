package com.java.note.Algorithm.yyy.sort;

/**
 * @Author : mmy
 * @Creat Time : 2020/5/3  8:05
 * @Description
 */
public class QuickSort {


    public static void main(String[] args) {
        int[] array = {1, 200, 3, 4, 1, 0, -1, 84, 31, 65};
        quickSort(array, 0, 9);
    }


    public static void quickSort(int[] array, int left, int right) {
        int i = left;
        int j = right;
        int mid = (i + j) / 2;
        int privot = array[mid];

        while (i <= j) {

            while (privot > array[i]) i++;

            while (privot < array[j]) j--;

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

}
