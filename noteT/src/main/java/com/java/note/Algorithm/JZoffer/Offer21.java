package com.java.note.Algorithm.JZoffer;

/**
 * @Author : mmy
 * @Creat Time : 2020/9/10  下午 3:00
 * @Description 调整数组顺序使奇数位于偶数前面
 * 需要保证奇数和奇数，偶数和偶数之间的相对位置不变，这和书本不太一样。
 */
public class Offer21 {
    public static void main(Strings[] args) {
        System.out.println();
    }


    public static int[] get(int[] h) {
        int left = 0;
        int right = h.length - 1;

        while (left < right) {

            while (left < right && h[left] % 2 == 1) left++;

            while (left < right && h[right] % 2 == 0) right--;


            if (left < right) {
                int temp = h[left];
                h[left] = h[right];
                h[right] = temp;
            }
        }
        return h;
    }
}
