package com.java.note.Algorithm.JZoffer;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author : mmy
 * @Creat Time : 2020/4/16  15:07
 * @Description 输入某二叉树的前序遍历和中序遍历的结果，请重建该二叉树。
 * 假设输入的前序遍历和中序遍历的结果中都不含重复的数字。
 * 前序遍历：3 9 20 15 7
 * 中序遍历：9 3 15 20 7
 * 1.递归
 */
public class Offer07 {

    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }

    public static void main(Strings[] args) {

    }

    // 缓存中序遍历数组每个值对应的索引
    private Map<Integer, Integer> indexOfInOder = new HashMap<>();

    public TreeNode buildTree(int[] preorder, int[] inorder) {
        for (int i = 0; i < inorder.length; i++) {
            indexOfInOder.put(inorder[i], i);
        }
        return reConstructBinaryTree(preorder, 0, preorder.length - 1, 0);
    }

    // 建立树-递归左右子树
    private TreeNode reConstructBinaryTree(int[] pre, int preL, int preR, int inL) {
        if (preL > preR) {
            return null;
        }

        TreeNode root = new TreeNode(pre[preL]);
        //拿到根节点在中序遍历中的索引
        int inIndexRoot = indexOfInOder.get(root.val);
        //根在左子树的距离
        int size = inIndexRoot - inL;
        root.left = reConstructBinaryTree(pre, preL + 1, preL + size, inL);
        root.right = reConstructBinaryTree(pre, preL + size + 1, pre.length - 1, inL + size + 1);
        return root;
    }

}


