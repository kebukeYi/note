package com.java.note.Algorithm.JZoffer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @Author : mmy
 * @Creat Time : 2020/9/11  上午 8:16
 * @Description 从上到下按层打印二叉树，同一层结点从左至右输出。每一层输出一行。 层序遍历
 */
public class Offer32 {

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


    public static int[] levelOrder(TreeNode pRoot) {
        if (pRoot == null) return new int[]{pRoot.val};
        ArrayList<Integer> ans = new ArrayList<>();
        //保存根节点
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(pRoot);


        while (!queue.isEmpty()) {

            //根节点
            TreeNode treeNode = queue.poll();
            ans.add(treeNode.val);
            //左子树
            if (treeNode.left != null) queue.add(treeNode.left);
            //右子树
            if (treeNode.right != null) queue.add(treeNode.right);
        }

        int[] res = new int[ans.size()];
        for (int i = 0; i < ans.size(); i++) {
            res[i] = ans.get(i);
        }
        return res;
    }

    //层序遍历二叉树
    ArrayList<ArrayList<Integer>> Print(TreeNode pRoot) {
        ArrayList<ArrayList<Integer>> results = new ArrayList<>();
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(pRoot);

        while (!queue.isEmpty()) {
            ArrayList<Integer> temp = new ArrayList<>();
            for (int i = queue.size(); i > 0; i--) {
                TreeNode treeNode = queue.poll();
                temp.add(treeNode.val);
                //左子树
                if (treeNode.left != null) queue.add(treeNode.left);
                //右子树
                if (treeNode.right != null) queue.add(treeNode.right);
            }

            results.add(temp);

        }
        return results;
    }

    //之字打印二叉树
    ArrayList<List<Integer>> zhiPrint(TreeNode pRoot) {
        ArrayList<List<Integer>> results = new ArrayList<>();
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(pRoot);

        while (!queue.isEmpty()) {
            LinkedList<Integer> temp = new LinkedList<>();

            for (int i = queue.size(); i > 0; i--) {
                TreeNode treeNode = queue.poll();

                if (results.size() % 2 == 0) {
                    temp.addLast(treeNode.val);
                } else {
                    temp.addFirst(treeNode.val);
                }
                //左子树
                if (treeNode.left != null) queue.add(treeNode.left);
                //右子树
                if (treeNode.right != null) queue.add(treeNode.right);
            }
            results.add(temp);

        }
        return results;
    }


}
