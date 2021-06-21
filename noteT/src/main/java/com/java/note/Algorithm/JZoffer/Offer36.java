package com.java.note.Algorithm.JZoffer;

/**
 * @Author : mmy
 * @Creat Time : 2020/9/11  上午 8:16
 * @Description 输入一棵二叉搜索树，将该二叉搜索树转换成一个排序的双向链表。
 * 要求不能创建任何新的结点，只能调整树中结点指针的指向。
 */
public class Offer36 {

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

    TreeNode head, pre;

    public TreeNode Convert(TreeNode root) {
        if (root == null) return null;
        dfs(root);//左根右
        head.left = pre;
        pre.right = head;
        return head;
    }

    private void dfs(TreeNode cur) {
        if (cur == null) return;//到底了
        //左子树
        dfs(cur.left);

        if (pre != null) pre.right = cur;
        else head = cur;

        cur.left = pre;
        pre = cur;


        //右子树
        dfs(cur.right);
    }


}
