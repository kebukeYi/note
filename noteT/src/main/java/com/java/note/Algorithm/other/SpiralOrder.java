package com.java.note.Algorithm.other;

import java.util.ArrayList;

/**
 * @author : kebukeyi
 * @date :  2021-06-29 18:15
 * @description : 螺旋遍历二维数组
 * @question :
 * @usinglink :
 **/
public class SpiralOrder {


    public static ArrayList<Object> spiralOrder(int[][] array) {
        if (array == null || array.length == 0 || array[0].length == 0) {
            return null;
        }
        ArrayList<Object> arrayList = new ArrayList<>();
        //行数
        int m = array.length;
        //列数
        int n = array[0].length;
        //二维数组的层数 取决与行列的较小值
        int size = (Math.min(m, n) + 1) / 2;
        //控制圈数
        for (int i = 0; i < size; i++) {

            //从左到右遍历 上侧
            //第一行 按照列走  控制右边界
            for (int j = i; j < n - i; j++) {
                //列变 行不变
                arrayList.add(array[i][j]);
                System.out.print(array[i][j] + " ");
            }

            //从上到下 右侧
            //首行跳过 逐行递减  控制底行
            for (int j = i + 1; j < m - i; j++) {
                //行变 列不变
                arrayList.add(array[j][(n - 1) - i]);
                System.out.print(array[j][(n - 1) - i] + " ");
            }

            //从右到左遍历 下侧
            //跳过最右侧的列
            for (int j = i + 1; (n - i) > j && (n - 1) > 2 * i; j++) {
                //列变  行不变
                arrayList.add(array[(m - 1) - i][(n - 1) - j]);
                System.out.print(array[(m - 1) - i][(n - 1) - j] + " ");
            }

            //从下到上 遍历左边
            //列不变行变
            for (int j = i + 1; (m - 1) - i > j && (m - 1) > 2 * i; j++) {
                //跳过最后一行
                arrayList.add(array[m - 1 - j][i]);
                System.out.print(array[m - 1 - j][i] + " ");
            }
        }
        return arrayList;
    }

    public static void main(String[] args) {
//        int[][] matrix = {
//                {1, 2, 3, 4, 5},
//                {6, 7, 8, 9, 10},
//                {11, 12, 13, 14, 15},
//                {16, 17, 18, 19, 20}
//        };
//        ArrayList<Object> objects = spiralOrder(matrix);
//        System.out.println(objects);
//        System.out.println();
        int[][] matrix2 = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9},
                {10, 11, 12},
                {13, 14, 15},
        };
        ArrayList<Object> objects = spiralOrder(matrix2);
        System.out.println();
        System.out.println(objects);
    }
}
 
