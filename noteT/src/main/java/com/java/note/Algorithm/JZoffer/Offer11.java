package com.java.note.Algorithm.JZoffer;

/**
 * @Author : mmy
 * @Creat Time : 2020/9/10  上午 8:51
 * @Description 把一个数组最开始的若干个元素搬到数组的末尾，我们称之为数组的旋转。
 * 输入一个非递减排序的数组的一个旋转，输出旋转数组的最小元素。
 */
public class Offer11 {

    public static void main(String[] args) {

    }

    //临界点 二分
    // 4 5 1 2 3 3 3 3
    //3 4 5 1 2 3 3 3
    //3 4 5 1 2
    public static int get(int[] nums) {
        int n = nums.length - 1;
        if (n < 0) return -1;
        //3 4 5 1 2 3 3 3
        while (n > 0 && nums[n] == nums[0]) n--;
        //可能是递增的
        if (nums[0] < nums[n]) return nums[0];

        int left = 0;
        int right = n;
        //二分
        while (left < right) {
            int mid = (left + right) / 2;

            if (nums[mid] < nums[0]) right = mid;
            else left = mid + 1;
        }

        return left;
    }

}
