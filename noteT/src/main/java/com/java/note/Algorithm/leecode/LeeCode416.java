package com.java.note.Algorithm.leecode;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author : fang.com
 * @CreatTime : 2021-01-20 14:27
 * @Description :
 * @Version :  0.0.1
 */
public class LeeCode416 {


    public static void main(String[] args) {
//        int[] a = new int[]{1, 11, 5, 2, 3};
//        int[] a = new int[]{1, 3, 4, 4};
//        int[] a = new int[]{1, 2};
//        int[] a = new int[]{1, 2, 4};
        int[] a = new int[]{2, 2, 1, 1};
        System.out.println(getSubArray(a).size());
    }


    public static List<Integer[]> getSubArray(int[] a) {
        // Arrays.sort(a);
        List<Integer[]> integers = new ArrayList<>(0);
        if (a.length == 2) {
            if (a[0] == a[1]) {
                integers.add(new Integer[]{1, 2, 3});
                return integers;
            } else {
                return integers;
            }
        }

        if (a == null || a.length == 0) {
            return new ArrayList<>(0);
        }
        //quickTwo(a, 0, a.length - 1);
        int right = a.length - 1;
        int left = 0;
        int subValue = 0;
        int rightValue = a[right];
        int leftValue = a[left];
        while (left < right) {
            //右边重   左边向里加
            if ((rightValue > leftValue)) {
                left++;
                leftValue = leftValue + a[left];
            }
            //左边重   右边向里加
            if ((leftValue > rightValue)) {
                right--;
                rightValue = rightValue + a[right];
            }
            //可以找到
            if ((rightValue == leftValue)) {
                if (right - left == 1) {
                    integers.add(new Integer[]{1, 2, 3});
                    break;
                } else {
                    left++;
                    leftValue = leftValue + a[left];
                }
            }
        }
        return integers;
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
