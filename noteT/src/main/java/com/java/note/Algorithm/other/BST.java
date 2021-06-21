package com.java.note.Algorithm.other;

import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * @Author : mmy
 * @Creat Time : 2020/4/17  11:43
 * @Description 二叉搜索树建立 以及各种遍历
 */
public class BST {

    private Node root;
    public static final int[] TREE_VALUE = new int[]{6, 2, 3, 1, 4, 5, 0, 0, 6, 0, 0, 7, 0, 0, 8, 0, 9, 10, 11, 12, 0};
    private static ArrayList arrayList = new ArrayList<>();
    public static int HGIH = 0;

    @Data
    private static class Node {
        int val;
        Node left;
        Node right;
        int N;//节点总数
        int hight = 0;//本节点高度
        boolean flag;

        public Node() {
        }

        public Node(int val, int n, int hight) {
            this.val = val;
            this.hight = hight;
            N = n;
        }
    }

    public int size(Node root) {
        if (root == null) return 0;
        else return root.N;
    }

    public int high(Node root) {
        if (root == null) return 0;
        else return root.hight;
    }

    public Node put(int value) {
        root = put(root, value, HGIH);
        return root;
    }

    // 创建搜索二叉树
    public Node put(Node root, int value, int hight) {
        if (root == null) return new Node(value, 1, 1);
        if (value > root.val) root.right = put(root.right, value, hight);
        if (value < root.val) root.left = put(root.left, value, hight);
        root.N = size(root.left) + size(root.right) + 1;
        root.hight = Math.max(high(root.right), high(root.left)) + 1;
        return root;
    }

    public static void main(Strings[] args) {
        BST bst = new BST();
        Node root = null;
        for (int i : TREE_VALUE) {
            root = bst.put(i);
        }
//        printPreOrder(root);
//        System.out.println();
//        printPreOrder2(root);
//        System.out.println(arrayList);
        printPostOrder(root);
        System.out.println();
        printPostOrder2(root);
//        System.out.println();
//        printLevelOrder(root);
//        System.out.println();
//        System.out.println(hight(root));
//        inCureNode(root);
//        System.out.println();
//        System.out.println(arrayList);
//        preCureNode(root);
    }

    //递归遍历  前序  二叉树
    public static void preCureNode(Node node) {
        //终止条件
        if (node == null) {
            return;
        }
        //逻辑
        printNode(node);
        preCureNode(node.left);
        preCureNode(node.right);
    }

    //递归遍历  中序 二叉树
    public static void inCureNode(Node node) {
        if (node == null) {
            return;
        }
        inCureNode(node.left);///1
        printNode(node);//2
        inCureNode(node.right);//3
    }

    //递归遍历  后序 二叉树
    public static void postCureNode(Node node) {
        if (node == null) {
            return;
        }
        postCureNode(node.left);
        postCureNode(node.right);
        printNode(node);
    }

    // 非递归遍历  先序遍历  利用栈1
    public static void printPreOrder(Node root) {
        Stack<Node> stack = new Stack<>();
        Node tempNode = root;
        while (true) {
            while (tempNode != null) {
                printNode(tempNode);
                stack.push(tempNode);
                tempNode = tempNode.left;
            }
            if (stack.isEmpty()) {
                break;
            }
            tempNode = stack.pop();
            tempNode = tempNode.right;
        }
    }

    // 非递归遍历  先序遍历  利用栈2
    public static void printPreOrder2(Node root) {
        Stack<Node> stack = new Stack<>();
        Node tempNode = root;
        while (tempNode != null || !stack.isEmpty()) {
            while (tempNode != null) {
                //进栈先打印
                printNode(tempNode);
                stack.push(tempNode);
                tempNode = tempNode.left;
            }
            if (!stack.isEmpty()) {
                tempNode = stack.pop();
                tempNode = tempNode.right;
            }

        }
    }

    // 非递归遍历 中序遍历 利用栈1
    public static void printInOder(Node root) {
        Stack<Node> stack = new Stack<>();
        Node tempNode = root;
        while (true) {
            while (tempNode != null) {
                stack.push(tempNode);
                tempNode = tempNode.left;
            }
            if (stack.isEmpty()) {
                break;
            }
            tempNode = stack.pop();
            printNode(tempNode);
            tempNode = tempNode.right;
        }
    }

    // 非递归遍历 中序遍历 利用栈2
    public static void printInOder2(Node root) {
        Stack<Node> stack = new Stack<>();
        Node tempNode = root;
        while (tempNode != null || !stack.isEmpty()) {
            while (tempNode != null) {
                stack.push(tempNode);
                tempNode = tempNode.left;
            }
            if (!stack.isEmpty()) {
                tempNode = stack.pop();
                //出栈打印
                printNode(tempNode);
                tempNode = tempNode.right;
            }
        }
    }

    // 非递归遍历 后序遍历 利用栈1
    public static void printPostOrder(Node root) {
        Stack<Node> stack = new Stack<>();
        Node tempNode = root;
        while (true) {
            while (tempNode != null) {
                if (tempNode.flag == true) {
                    tempNode = tempNode.right;
                } else {
                    stack.push(tempNode);
                    tempNode = tempNode.left;
                }
            }
            tempNode = stack.pop();
            if (tempNode.flag == false) {//左边没搞完
                stack.push(tempNode);
                tempNode.flag = true;
                tempNode = tempNode.right;
            } else {//左边搞完  该搞右边的了
                printNode(tempNode);
                if (stack.isEmpty()) {
                    break;
                }
                tempNode = stack.peek();
                tempNode.flag = true;//左子树搞完了
            }
        }
    }


    // 非递归遍历 后序遍历 利用栈2
    public static void printPostOrder2(Node root) {
        Stack<Node> stack = new Stack<>();
        Node tempNode = root;
        stack.push(root);
        stack.push(root);
        while (!stack.isEmpty()) {
            Node node = stack.pop();
            if (!stack.isEmpty() && node == stack.peek()) {
                if (node.right != null) {
                    stack.push(node.right);
                    stack.push(node.right);
                }
                if (node.left != null) {
                    stack.push(node.left);
                    stack.push(node.left);
                }
            } else {
                System.out.println(node.val);
            }
        }
    }


    // 层序遍历 利用队列
    public static void printLevelOrder(Node root) {
        Queue<Node> queue = new LinkedList<>();
        Node tempNode = root;
        queue.offer(tempNode);
        System.out.println(queue);
        while (!queue.isEmpty()) {
            Node topNode = queue.poll();
            if (topNode == null) continue;
            printNode(topNode);
            queue.offer(topNode.left);
            queue.offer(topNode.right);
        }
    }

    //递归 树高
    public static int hight(Node root) {
        //终结条件
        if (root == null) {
            return 0;
        }
        int left = hight(root.left);
        int right = hight(root.right);
        return Math.max(left, right) + 1;
    }

    public static void printNode(Node node) {
        if (node == null) {
            System.out.print("*");
        } else {
            arrayList.add(node.val);
            System.out.print(node.val + "-");
        }
    }
}
