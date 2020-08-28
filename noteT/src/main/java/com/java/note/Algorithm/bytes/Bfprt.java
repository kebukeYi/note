package com.java.note.Algorithm.bytes;

import java.util.Arrays;

/**
 * @Author : mmy
 * @Creat Time : 2020/4/24  10:46
 * @Description 在BFPTR算法中，仅仅是改变了快速排序Partion中的pivot值的选取，在快速排序中，我们始终选择第一个元素或者最后一个元素作为pivot，
 * 而在BFPTR算法中，每次选择五分中位数的中位数作为pivot，这样做的目的就是使得划分比较合理，从而避免了最坏情况的发生。算法步骤如下
 * <p>
 * 1. 将 [公式] 个元素划为 [公式] 组，每组5个，至多只有一组由 [公式] 个元素组成。
 * 2. 寻找这 [公式] 个组中每一个组的中位数，这个过程可以用插入排序。
 * 3. 对步骤2中的 [公式] 个中位数，重复步骤1和步骤2，递归下去，直到剩下一个数字。
 * 4. 最终剩下的数字即为pivot，把大于它的数全放左边，小于等于它的数全放右边。
 * 5. 判断pivot的位置与k的大小，有选择的对左边或右边递归。
 */
public class Bfprt {

    public static int getMinKthByBFPRT(int[] arr, int k) {
        int[] copyArr = new int[arr.length];
        copyArr = copyArray(arr);
        return bfprt(copyArr, 0, copyArr.length - 1, k - 1);
    }

    public static int[] copyArray(int[] arr) {
        int[] tmp = new int[arr.length];
        for (int i = 0; i != arr.length; i++)
            tmp[i] = arr[i];
        return tmp;
    }

    public static int bfprt(int[] arr, int begin, int end, int i) {//begin到end范围内求第i小的数
        if (begin == end)
            return arr[begin];
        int pivot = medianOfMedians(arr, begin, end);//中位数作为划分值
        int[] pivotRange = partition(arr, begin, end, pivot);//进行划分，返回等于区域
        if (i >= pivotRange[0] && i <= pivotRange[1])
            return arr[i];
        else if (i < pivotRange[0])
            return bfprt(arr, begin, pivotRange[0] - 1, i);
        else
            return bfprt(arr, pivotRange[1] + 1, end, i);
    }

    public static int medianOfMedians(int[] arr, int begin, int end) {
        int num = end - begin + 1;
        int offset = num % 5 == 0 ? 0 : 1;
        int[] mArr = new int[num / 5 + offset];
        for (int i = 0; i < mArr.length; i++) {
            int beginI = begin + i * 5;
            int endI = beginI + 4;
            mArr[i] = getMedian(arr, beginI, Math.min(end, endI));
        }
        return bfprt(mArr, 0, mArr.length - 1, mArr.length / 2);
    }

    public static int getMedian(int[] arr, int begin, int end) {
        Arrays.sort(arr, begin, end);
        int sum = end + begin;
        int mid = (sum / 2) + (sum % 2);
        return arr[mid];
    }

    public static void insertSort(int[] arr, int begin, int end) {
        for (int i = begin + 1; i != end + 1; i++) {
            for (int j = i; j != begin; j--) {
                if (arr[j - 1] > arr[j])
                    swap(arr, j - 1, j);
                else
                    break;
            }
        }
    }

    public static int[] partition(int[] arr, int begin, int end, int pivotValue) {
        int small = begin - 1;
        int cur = begin;
        int big = end + 1;
        while (cur != big) {
            if (arr[cur] < pivotValue)
                swap(arr, ++small, cur++);
            else if (arr[cur] > pivotValue)
                swap(arr, cur, --big);
            else
                cur++;
        }
        int[] range = new int[2];
        range[0] = small + 1;
        range[1] = big - 1;
        return range;
    }

    public static void swap(int[] arr, int i, int j) {
        int t = arr[i];
        arr[i] = arr[j];
        arr[j] = t;
    }

    public int findKthLargest(int[] nums, int k) {
        return getMinKthByBFPRT(nums, nums.length - k + 1);
    }

    public static void main(String[] args) {
        int[] a = {1, 1, -1, -1, -1, -2, -1, 2, 3, 3, 4, 5, 6};
//        int[] a = {1, -1, 2, 3, 3, 4, 5, 6};
        // -1 -1 -2 1 1 2 3 3 4 5 6
        //第2小的数值
        int k = 2;
        //打印 -1
        System.out.println(getMinKthByBFPRT(a, k));

    }


}
