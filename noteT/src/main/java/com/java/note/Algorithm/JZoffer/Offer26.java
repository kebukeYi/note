package com.java.note.Algorithm.JZoffer;

/**
 * @Author : mmy
 * @Creat Time : 2020/9/11  上午 8:16
 * @Description 输入两棵二叉树A，B，判断B是不是A的子结构。（ps：我们约定空树不是任意一个树的子结构）
 */
public class Offer26 {

    public class TreeNode {
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


    public boolean HasSubtree(TreeNode A, TreeNode B) {
        //A B  两根
        // A左子树 B
        //A 右子树 B
        if (A == null || B == null) return false;
        return (A != null && B != null) && (isChild(A, B) || HasSubtree(A.left, B) || HasSubtree(A.right, B));

    }

    private boolean isChild(TreeNode root1, TreeNode root2) {
        if (root2 == null) return true;
        if (root1 == null || root1.val != root2.val) return false;

        return isChild(root1.right, root2.right) && isChild(root1.left, root2.left);
    }


}
