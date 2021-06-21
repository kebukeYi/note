package com.java.note.Algorithm.leecode;

/**
 * @Author : mmy
 * @Creat Time : 2020/3/30  9:20
 * @Description 移除重复元素  滑动窗口
 */
public class Solution {

    // nums = [0,0,0,1,1,1,2,2,3,3,4]
    //        [null,null,0,null,null,1,null,2,null,3,4]
    // nums = [0,1,2,2,3,3,4]
    //        [0,1,null,2,null,4]
    public static int removeDuplicates(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        int back = 0;
        for (int front = 1; front < nums.length; front++) {
            if (nums[back] != nums[front]) {
                back++;
                nums[back] = nums[front];
            }
        }
        return back + 1;
    }


    public static int removeDuplicatesTwo(int[] nums) {
        int start = 0;
        for (int i = 1; i < nums.length; i++) {
            if (nums[start] != nums[i]) {
                nums[start] = nums[i];
                start++;
            }
        }
        return 1;
    }

    public static void main(Strings[] args) {
        int[] nums = new int[]{0, 0, 0, 1, 1, 1, 2, 2, 3, 4};
//        int[] nums = new int[]{0, 0, 1, 2, 2, 3,3,3, 4,5};
//        int[] nums = new int[]{0, 1, 2, 3, 4, 5 ,5, 6};//8个元素
        System.out.println(removeDuplicates(nums));
    }
}




