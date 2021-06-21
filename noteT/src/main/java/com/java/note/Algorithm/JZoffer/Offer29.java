package com.java.note.Algorithm.JZoffer;

import java.util.ArrayList;

/**
 * @Author : mmy
 * @Creat Time : 2020/9/11  上午 8:16
 * @Description 顺时针打印矩阵
 */
public class Offer29 {

    public static void main(Strings[] args) {
        System.out.println();
    }


    public ArrayList<Integer> printMatrix(int[][] matrix) {
        ArrayList<Integer> rel = new ArrayList<>();
        int top = 0;
        int bottom = matrix.length - 1;

        int left = 0;
        int right = matrix[0].length - 1;


        while (top <= bottom && left <= right) {

            for (int i = left; i <= right; i++) {
                rel.add(matrix[top][i]);
            }

            for (int i = top+1; i <= bottom; i++) {
                rel.add(matrix[i][right]);
            }

            for (int i = right-1; i >=left; i--) {
                rel.add(matrix[bottom][i]);
            }

            for (int i = bottom-1; i > top; i--) {
                rel.add(matrix[i][left]);
            }

            left++;
            right--;
            top++;
            bottom--;
        }
        return rel;

    }

    public ArrayList<Integer> printMatrix1(int[][] matrix) {
        ArrayList<Integer> rel = new ArrayList<>();
        int r1 = 0, r2 = matrix.length - 1, c1 = 0, c2 = matrix[0].length - 1;

        while (r1 <= r2 && c1 <= c2) {

            for (int i = c1; i <= c2; i++)
                rel.add(matrix[r1][i]);

            for (int i = r1 + 1; i <= r2; i++)
                rel.add(matrix[i][c2]);

            if (r1 != r2)
                for (int i = c2 - 1; i >= c1; i--)
                    rel.add(matrix[r2][i]);

            if (c1 != c2)
                for (int i = r2 - 1; i > r1; i--)
                    rel.add(matrix[i][c1]);


            r1++;
            r2--;
            c1++;
            c2--;
        }

        return rel;
    }
}
