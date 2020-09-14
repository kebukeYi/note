package com.java.note.Algorithm.JZoffer;

/**
 * @Author : mmy
 * @Creat Time : 2020/9/10  上午 7:58
 * @Description 给定一个二叉树和其中的一个结点，请找出中序遍历顺序的下一个结点并且返回。注意，树中的结点不仅包含左右子结点，同时包含指向父结点的指针。
 * 给一个层序遍历：【2，1，3，null,null,null,null,null】
 */
public class Offer08 {

    public class TreeLinkNode {
        int val;
        TreeLinkNode left = null;
        TreeLinkNode right = null;
        TreeLinkNode pre = null;

        TreeLinkNode(int val) {
            this.val = val;
        }
    }

    /**
     * 按照中序遍历的节点特点：下一个节点应该是：
     * 判断右节点是否为空：
     * ① 如果一个节点的右子树不为空，那么该节点的下一个节点是右子树的最左节点；
     * ② 否则，向上找第一个左链接 指向的树包含该节点的祖先节点。
     */
    public static TreeLinkNode get(TreeLinkNode p) {
        //左根右 特点

        //该节点的下一个节点是右子树的最深的最左节点；
        if (p.right != null) {
            p = p.right;
            //寻找最深左节点
            while (p.left != null) {
                p = p.left;
            }
            return p;
        }

        //查看父节点是否为空
        while (p.pre != null) {
            //当前节点是否为父节点的左节点
            if (p == p.pre.left) {
                return p.pre;
            }
            //那么就是父节点的右节点，就需要
            p = p.pre;
        }
        return null;
    }


}
