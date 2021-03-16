package com.java.note.Algorithm.other;

/**
 * @Author : fang.com
 * @CreatTime : 2021-03-15 17:28
 * @Description :
 * @Version :  0.0.1
 */
public class binarySearch {

    static int findFirstEqualLarger(int[] array, double key) {
        int left = 0;
        int right = array.length - 1;

        // 这里必须是 <=
        while (left <= right) {
            int mid = (left + right) / 2;
            if (array[mid] >= key) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }

    public static void main(String[] args) {
        int[] ad = new int[]{1, 2, 3, 4, 5, 6};
        System.out.println(findFirstEqualLarger(ad, 3.5));
    }
}
