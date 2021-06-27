package com.java.note.Algorithm.JZoffer;

/**
 * @Author : mmy
 * @Creat Time : 2020/9/11  上午 8:16
 * @Description 输入一个整数数组，判断该数组是不是某二叉搜索树的后序遍历的结果。假设输入的数组的任意两个数字都互不相同。
 * 左右根
 */
public class Offer33 {

    class TreeNode {

        int val = 0;
        Offer32.TreeNode left = null;
        Offer32.TreeNode right = null;

        public TreeNode(int val) {
            this.val = val;
        }
    }

    public static void main(String[] args) {
        System.out.println();
    }


    public boolean VerifySquenceOfBST(int[] sequence) {
        return dfs(sequence, 0, sequence.length - 1);
    }

    private boolean dfs(int[] sequence, int left, int right) {
        //遍历到最后都是相同的
        if (left >= right) return true;
        int p = left;
        //一般right 为root 节点

        //寻找左右子树分界点
        while (sequence[p] < sequence[right]) p++;
        int m = p;
        while (sequence[p] > sequence[right]) p++;

        return (p == right) && dfs(sequence, left, m - 1) && dfs(sequence, m, right - 1);
    }
}
