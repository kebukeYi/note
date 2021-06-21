package com.java.note.Algorithm.acwing;

/**
 * @Author : mmy
 * @Creat Time : 20 20/9/9  下午 5:47
 * @Description 蛇形矩阵
 */
public class ACone {

    public static void main(Strings[] args) {
        int N = 20;
        int n = 6;
        Integer[][] array = new Integer[N][N];
        printMain(n, array);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(array[i][j] + " ");
            }
            System.out.println();
        }
    }


    public static void printMain(int n, Integer[][] arrays) {
        int left = 0, top = 0;
        int right = n - 1, bottom = n - 1;
        int k = 1;
        while (left <= right && top <= bottom) {

            //上侧一行
            for (int i = left; i <= right; i++) {
                arrays[top][i] = k++;
            }

            //右侧一列
            for (int i = top + 1; i <= bottom; i++) {
                arrays[i][right] = k++;
            }

            //底侧一行
            for (int i = right - 1; i >= left; i--) {
                arrays[bottom][i] = k++;
            }

            //左侧一列
            // i > top 没有 = 号 是不能走到底
            for (int i = bottom - 1; i > top; i--) {
                arrays[i][left] = k++;
            }

            left++;
            right--;
            top++;
            bottom--;
        }
    }


    /**
     * 1.出界
     * 2. 判断重复格子
     */
    public static void ACwing(int n, Integer[][] arrays) {


    }


}
