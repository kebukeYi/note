package com.java.note.Algorithm.JZoffer;

/**
 * @Author : mmy
 * @Creat Time : 2020/4/16  13:31
 * @Description 在一个 n * m 的二维数组中，每一行都按照从左到右递增的顺序排序，每一列都按照从上到下递增的顺序排序。
 * 请完成一个函数,输入这样的一个二维数组和一个整数，判断数组中是否含有该整数。
 */
public class Offer04 {
    public static void main(String[] args) {

    }

    public boolean findNumberIn2DArray(int[][] matrix, int target) {
        if (matrix.length == 0) {
            return false;
        }
        if (matrix[0].length == 0) {
            return false;
        }

        int length = matrix.length - 1;
        //从一方最大开始，最下层开始
        int rows = length;
        int column = 0;

        while (rows >= 0 && column < matrix[0].length) {
            int result = matrix[rows][column];
            //输入值 大于 目前值
            if (target > result) {
                //列右移
                column++;

                //当前值 大于 输入值
            } else if (target < result) {
                //行上移
                rows--;
            } else {
                return true;
            }
        }
        return false;
    }
}
