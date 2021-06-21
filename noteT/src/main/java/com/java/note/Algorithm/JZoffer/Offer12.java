package com.java.note.Algorithm.JZoffer;

/**
 * @Author : mmy
 * @Creat Time : 2020/9/10  上午 8:51
 * @Description 判断在一个矩阵中是否存在一条包含某字符串所有字符的路径。
 * 路径可以从矩阵中的任意一个格子开始，每一步可以在矩阵中向上下左右移动一个格子。
 * 如果一条路径经过了矩阵中的某一个格子，则该路径不能再进入该格子
 */
public class Offer12 {

    public static void main(Strings[] args) {

    }


    //回溯算法
    public static boolean get(char[][] board, Strings word) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (dfs(board, word, 0, i, j)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean dfs(char[][] board, Strings word, int u, int x, int y) {
        //先做边界检查
        if (x >= board.length || x < 0 || y > board[0].length || y < 0 || board[x][y] != word.charAt(u)) return false;
        if (u == word.length() - 1) return true;

        char temp = board[x][y];
        board[x][y] = '*';
        boolean res = dfs(board, word, u + 1, x - 1, y) || dfs(board, word, u + 1, x + 1, y)
                || dfs(board, word, u + 1, x, y - 1) || dfs(board, word, u + 1, x, y + 1);

        board[x][y] = temp;
        return res;
    }
}
