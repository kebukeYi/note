package com.java.note.Algorithm.leecode;

/**
 * @Author : mmy
 * @Creat Time : 2020/4/21  22:37
 * @Description 假设按照升序排序的数组在预先未知的某个点上进行了旋转。
 * <p>
 * ( 例如，数组 [0,1,2,4,5,6,7] 可能变为 [4,5,6,7,0,1,2] )。
 * <p>
 * 搜索一个给定的目标值，如果数组中存在这个目标值，则返回它的索引，否则返回 -1 。
 * <p>
 * 你可以假设数组中不存在重复的元素。
 */
public class LeeCode33 {

    /*
    困难二分查找
     */
    public static int search(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        while (left < right) {
            int mid = (left + right) / 2;
            if (nums[mid] == target) {
                return mid;
            }
            if (nums[0] < nums[mid] && (target > nums[mid] || target < nums[0])) {//[0,mid]  有序时 ：在左边递增 并且比 tar 大
                left = mid + 1;
            } else if (nums[0] > nums[mid] && target > nums[mid]) {//发生旋转
                left = mid + 1;
            } else {
                right = right;
            }
        }
        return left == right && nums[left] == target ? left : -1;
    }

    public static void main(String[] args) {
        int[] nums = {9, 10, 11, 12, 1, 2, 5, 8};
        System.out.println(search(nums, 5));
    }
}
