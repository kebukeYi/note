package com.java.note.Algorithm.leecode;

import java.util.Scanner;

/**
 * @Author : mmy
 * @Creat Time : 2020/4/14  16:04
 * @Description 蛇形矩阵
 * 4个方向  4个偏移量
 * 什么时候 开始转换方向 ： 走出边界  走到了被占用的格子
 */
public class SnakeMatrix {

    final static int N = 110;

    public static void main(String[] args) {
        int m = 0;//列数
        int n = 0;//行数
        int[][] res = new int[N][N];
        boolean[][] st = new boolean[N][N];
        Scanner scanner = new Scanner(System.in);
        n = scanner.nextInt();
        m = scanner.nextInt();

        int dx[] = {0, 1, 0, -1}, dy[] = {1, 0, -1, 0};//4个偏移量

        int x = 0, y = 0, d = 0;//初始位置 和  d=下一个方向
        for (int i = 1; i <= m * n; i++) {//从1开始
            int nx = x + dx[d], ny = y + dy[d];//行
            System.out.println("nx:" + nx + "     ny:" + ny);
            if (nx < 0 || nx >= n || ny < 0 || ny >= m || st[nx][ny]) {
                System.out.println("换方向");
                d = (d + 1) % 4;
                nx = x + dx[d];
                ny = y + dy[d];
            }
            res[x][y] = i;
            st[x][y] = true;

            x = nx;
            y = ny;
        }
        for (int j = 0; j < n; j++) {
            for (int h = 0; h < m; h++) {
                System.out.print(res[j][h]);
            }
            System.out.println();
        }

    }


}
