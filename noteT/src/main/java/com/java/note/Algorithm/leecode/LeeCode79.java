package com.java.note.Algorithm.leecode;

/**
 * @Author : mmy
 * @Creat Time : 2020/4/22  16:10
 * @Description 单词搜索Ⅰ
 * 给定一个二维网格和一个单词，找出该单词是否存在于网格中。
 * <p>
 * 单词必须按照字母顺序，通过相邻的单元格内的字母构成，其中“相邻”单元格是那些水平相邻或垂直相邻的单元格。同一个单元格内的字母不允许被重复使用。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/word-search
 * <p>
 * 这是一个使用回溯算法解决的问题，涉及的知识点有 DFS 和状态重置。
 */
public class LeeCode79 {

    private boolean[][] marked;
    private int[][] direction = {{-1, 0}, {-1, -1}, {1, 0}, {0, 1}};
    int m, n;
    private Strings word;//apple
    private char[][] borad;

    public boolean exist(char[][] board, Strings word) {
        n = board.length;
        if (n == 0) {
            return false;
        }
        m = board[0].length;
        marked = new boolean[n][m];
        this.borad = board;
        this.word = word;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                System.out.println(i + "," + j);
                if (DFS(i, j, 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    /*
    深度遍历
     */
    boolean DFS(int i, int j, int index) {
        if (index == word.length() - 1) {
            return borad[i][j] == word.charAt(index);
        }
        if (borad[i][j] == word.charAt(index)) {
            marked[i][j] = true;
            for (int k = 0; k < 4; k++) {//在 [i，j ] 点  四个方向搜寻  有的话就直接返回 true  没有的话
                int nx = direction[k][0] + i;
                int ny = direction[k][1] + j;
                if (nx >= 0 && ny >= 0 && nx < n && ny < m && !marked[nx][ny]) {
                    System.out.println(nx + " -  " + ny);
                    if (DFS(nx, ny, index + 1)) {
                        return true;
                    }
                }
            }
            System.out.println(i + "  " + j + "->" + borad[i][j] + " - false");
            marked[i][j] = false;
        }
        System.out.println(i + "  " + j + " 不行");
        return false;
    }

    public static void main(Strings[] args) {

        char[][] board = {
                {'A', 'B', 'C', 'E'},
                {'S', 'F', 'C', 'S'},
                {'A', 'D', 'E', 'E'}
        };

        Strings word = "ABCCEF";


//        char[][] board = {{'a', 'b'}};
//        String word = "ba";
        LeeCode79 solution = new LeeCode79();
        boolean exist = solution.exist(board, word);
        System.out.println(exist);
        for (boolean[] a : solution.marked) {
            for (boolean b : a) {
                System.out.print(b + "  ");
            }
            System.out.println();
        }

    }


}
