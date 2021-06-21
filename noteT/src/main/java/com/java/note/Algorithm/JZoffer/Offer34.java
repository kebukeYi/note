package com.java.note.Algorithm.JZoffer;

import java.util.ArrayList;

/**
 * @Author : mmy
 * @Creat Time : 2020/9/11  上午 8:16
 * @Description 输入一颗二叉树和一个整数，。打印出二叉树中结点值的和为输入整数的所有路径
 * 路径定义为从树的根结点开始往下一直到叶结点所经过的结点形成一条路径。
 */
public class Offer34 {

    class TreeNode {
        int val = 0;
        TreeNode left = null;
        TreeNode right = null;

        public TreeNode(int val) {
            this.val = val;
        }
    }

    public static void main(Strings[] args) {
        System.out.println();
    }


    //保存所有路径
    private ArrayList<ArrayList<Integer>> ret = new ArrayList<>();

    public ArrayList<ArrayList<Integer>> FindPath(TreeNode root, int target) {
        //递归函数
        backtracking(root, target, new ArrayList<Integer>());
        return ret;
    }

    private void backtracking(TreeNode root, int target, ArrayList<Integer> path) {
        if (root == null) return;
        target -= root.val;
        path.add(root.val);

        //找到路径
        if (target == 0 && root.left == null && root.right == null)
            ret.add(new ArrayList<>(path));

        backtracking(root.left, target, path);
        backtracking(root.right, target, path);

        path.remove(path.size() - 1);

    }
}
