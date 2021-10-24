package com.java.note.Algorithm.zhengge.one_week;

/**
 * @author : kebukeYi
 * @date :  2021-10-22 16:27
 * @description:
 * @question:
 * @link:
 **/
public class ArrayDemo {

    //反转数组
    public static void reverse(int[] a) {
        if (a == null || a.length == 1) return;
        int n = a.length - 1;
        for (int i = 0; i <= n / 2; ++i) {
            int temp = a[i];
            a[i] = a[n - i];
            a[n - i] = temp;
        }
    }

    public static void main(String[] args) {
//        int array[] = new int[]{1, 2, 3, 4, 5, 6,7,8};
        int array[] = new int[]{1};
//        int array[] = new int[]{1,2,3};
//        int array[] = new int[]{1, 2, 3, 4};
        reverse(array);
        for (int i : array) {
            System.out.print(i + "  ");
        }
    }
}
 
