package com.java.note.Algorithm.leecode;

import lombok.Data;

/**
 * @Author : fang.com
 * @CreatTime : 2021-03-05 16:22
 * @Description : 124 题
 * @Version :  0.0.1
 */
public class LeeCode124 {

    @Data
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode() {
        }

        TreeNode(int val) {
            this.val = val;
        }

        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }


    public static TreeNode creatTreeNode() {
        TreeNode root = new TreeNode(-10);
        TreeNode leftRoot = new TreeNode(9);
        TreeNode rightRoot = new TreeNode(20);
        TreeNode rightRootleft = new TreeNode(15);
        TreeNode rightRootright = new TreeNode(7);
        TreeNode leftRootright = new TreeNode(-7);
        TreeNode leftRootleft = new TreeNode(-4);
        root.left = leftRoot;
        root.right = rightRoot;
        rightRoot.left = rightRootleft;
        rightRoot.right = rightRootright;
        leftRoot.left = leftRootleft;
        leftRoot.right = leftRootright;
        return root;
    }

    public static int ret = Integer.MIN_VALUE;

    public static void main(Strings[] args) {
        System.out.println(pathMax(creatTreeNode()));
    }

    public static int pathMax(TreeNode root) {
        getMax(root);
        return ret;
    }

    //             -10
    //         9          20
    //     -4  -7   15     7
    private static Integer getMax(TreeNode root) {
        if (root == null) return 0;
        // 如果 子树路径和为负则应当置0表示最大路径不包含此子树
        int left = Math.max(0, getMax(root.left));
        int right = Math.max(0, getMax(root.right));
        // 判断 该节点+左右子树 的路径和是否大于当前最大路径和
        ret = Math.max(ret, root.val + left + right);
        // return Math.max(left, right) + root.val;
        return root.val + left + right;
    }

}
