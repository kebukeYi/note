package com.java.note.Algorithm.JZoffer;

/**
 * @Author : mmy
 * @Creat Time : 2020/9/11  上午 8:16
 * @Description 请实现一个函数，用来判断一棵二叉树是不是对称的。
 * 注意，如果一个二叉树同此二叉树的镜像是同样的，定义其为对称的。
 */
public class Offer28 {

    class TreeNode {
        int val = 0;
        TreeNode left = null;
        TreeNode right = null;

        public TreeNode(int val) {
            this.val = val;
        }
    }

    public static void main(String[] args) {
        System.out.println();
    }

    //深度优先遍历
    boolean isSymmetrical(TreeNode pRoot) {
        if (pRoot == null) return false;

        return dfs(pRoot.left, pRoot.right);
    }

    private boolean dfs(TreeNode left, TreeNode right) {
        if (left == null && right == null) return true;
        if (left == null || right == null) return false;
        return dfs(left.left, right.right) && dfs(left.right, right.left);
    }
}
