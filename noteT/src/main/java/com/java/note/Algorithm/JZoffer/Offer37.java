package com.java.note.Algorithm.JZoffer;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @Author : mmy
 * @Creat Time : 2020/9/11  上午 8:16
 * @Description 请实现两个函数，分别用来序列化和反序列化二叉树。
 */
public class Offer37 {

    class TreeNode {
        int val = 0;
        TreeNode left = null;
        TreeNode right = null;

        public TreeNode(int val) {
            this.val = val;
        }
    }


    String Serialize(TreeNode root) {
        if (root == null) return "[ ]";
        StringBuilder res = new StringBuilder("[");
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);

        //层序遍历
        while (!queue.isEmpty()) {
            TreeNode t = queue.poll();
            if (t != null) {
                res.append(t.val + ",");
                queue.add(t.left);
                queue.add(t.right);
            } else {
                res.append("null,");
            }
        }


        return res.toString();
    }

    TreeNode Deserialize(String str) {
        if (str.equals("[ ]")) {
            return null;
        }

        String[] vals = str.substring(1, str.length() - 1).split(",");
        TreeNode root = new TreeNode(Integer.parseInt(vals[0]));

        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        int i = 1;

        while (!queue.isEmpty()) {
            TreeNode treeNode = queue.poll();

            if (!vals[i].equals("null")) {
                treeNode.left = new TreeNode(Integer.parseInt(vals[i]));
                queue.add(treeNode.left);
            }
            i++;
            if (!vals[i].equals("null")) {
                treeNode.right = new TreeNode(Integer.parseInt(vals[i]));
                queue.add(treeNode.right);
            }
            i++;
        }
        return root;
    }


}
