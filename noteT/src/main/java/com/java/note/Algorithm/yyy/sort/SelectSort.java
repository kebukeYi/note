package com.java.note.Algorithm.yyy.sort;

import com.java.note.Algorithm.mt.Array;

import java.util.Arrays;
import java.util.List;

/**
 * @author : kebukeyi
 * @date :  2021-06-28 16:58
 * @description : 选择排序
 * @question :
 * @usinglink :
 **/
public class SelectSort {

    //时间复杂度 O(n²)
    //空间复杂度 O(1)
    public static void selectSort(int[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            //每次选出第一个来进行交换
            int minIndex = i;
            //找除了开头的剩下的最小的
            for (int j = i + 1; j < array.length; j++) {
                if (array[j] < array[minIndex]) {
                    minIndex = j;
                }
            }
            //后面是 存在最小的
            if (i != minIndex) {
                //两数交换
//                int i1 = array[i];
//                array[i] = array[minIndex];
//                array[minIndex] = i1;
                array[i] ^= array[minIndex];
                array[minIndex] ^= array[i];
                array[i] ^= array[minIndex];
            }
        }
    }

    public static void main(String[] args) {
        //不稳定性
        int[] array = new int[]{3, 6, 1, 2, 88, 33, -2, -7, 55};
        selectSort(array);
        System.out.println(Arrays.toString(array));

    }


}
 
