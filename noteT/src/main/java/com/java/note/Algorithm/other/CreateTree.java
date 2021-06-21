package com.java.note.Algorithm.other;

import lombok.Data;

/**
 * @Author : mmy
 * @Creat Time : 2020/4/17  10:47
 * @Description 普通二叉树建立
 */
public class CreateTree {

    @Data
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        public TreeNode() {
        }

        TreeNode(int x) {
            val = x;
        }
    }

    public static int index = 1;
    public static final int[] TREE_VALUE = new int[]{1, 2, 3, 0, 4, 5, 0, 0, 6, 0, 0, 7, 0, 0, 8, 0, 9, 10, 0, 0, 0};

    //前序创建
    public static TreeNode inCreateTree(int level, TreeNode node) {
        //终止条件
        if (TREE_VALUE[level] == 0) {
            return null;
        } else {
            node.setVal(TREE_VALUE[level]);
        }

        //处理逻辑
        TreeNode leftChild = new TreeNode();
        node.left = inCreateTree(++index, leftChild);
        TreeNode rightChild = new TreeNode();
        node.right = inCreateTree(++index, rightChild);

        return node;
    }

    public static void preCreateTree(Strings[] args) {

    }

    public static void postCreateTree(Strings[] args) {

    }

    public static void main(Strings[] args) {
        TreeNode treeNode = new TreeNode();
        treeNode = inCreateTree(index, treeNode);

    }

}
