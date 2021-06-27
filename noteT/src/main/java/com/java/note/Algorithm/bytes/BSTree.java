package com.java.note.Algorithm.bytes;

import lombok.Data;

import java.util.Random;

/**
 * @Author : mmy
 * @Creat Time : 2020/4/20  17:10
 * @Description 重建二叉树
 */
public class BSTree {

    static Random random = new Random();

    @Data
    static class Node {
        int data;
        Node left;
        Node right;


        public Node(int data) {
            this.data = data;
        }
    }

    public Node processOne(int[] possAtr, int L, int R) {
        //终止条件
        if (L > R) return null;
        int headData = possAtr[R];
        Node head = new Node(headData);
        if (L == R) return head;
        //处理逻辑
//        int M = R - 1;//开始遍历节点 找到小于头节点的最后一个节点
        int M = L - 1;//3 种情况 ： 1：存在大于和小于的点在尾节点之前 ；2：当只有小于时，没有右树，M会更新 ；3：只有大于的  没有左树  递归会返回null
        // 时间复杂度 ：                                                                 O(N^2)
        for (int i = L; i < R; i++) {
            if (possAtr[i] < headData) {
                M = i;
            }
        }
        head.left = processOne(possAtr, L, M);
        head.right = processOne(possAtr, M + 1, R - 1);
        return head;
    }

    /*
    采用二分法查找位置
     */
    public Node processThree(int[] possAtr, int L, int R) {
        //终止条件
        if (L > R) return null;
        int headData = possAtr[R];
        Node head = new Node(headData);
        if (L == R) return head;
        //处理逻辑
//        int M = R - 1;//开始遍历节点 找到小于头节点的最后一个节点
        int M = L - 1;//3 种情况 ： 1：存在大于和小于的点在尾节点之前 ；2：当只有小于时，没有右树，M会更新 ；3：只有大于的  没有左树  递归会返回null

        int left = L;
        int right = R - 1;
        while (left <= right) {
            int mid = left + (right + left) >> 1;
            if (possAtr[mid] < possAtr[R]) {
                M = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        for (int i = L; i < R; i++) {
            if (possAtr[i] < headData) {
                M = i;
            }
        }
        head.left = processThree(possAtr, L, M);
        head.right = processThree(possAtr, M + 1, R - 1);
        return head;
    }


    /*
    建立随机二叉树
     */
    public static Node generaRandomBTS(int min, int max, int level, int N) {
        return createTree(min, max, level, N);
    }

    /*
    随机二叉树搜索树
     */
    public static Node createTree(int min, int max, int level, int N) {
        if (min > max || level > N) {
            return null;
        }
        Node head = new Node(random(min, max));
        head.left = createTree(min, head.data - 1, level + 1, N);//左节点范围： 从最小节点到 头节点的减一范围  下一层见
        head.right = createTree(head.data + 1, max, level + 1, N);// 右节点范围： 比头节点 大的范围 到max 范围 下一层见
        return head;
    }

    public static int random(int min, int max) {
        return (int) (min + Math.random() * (max - min + 1));
    }

    public static void main(String[] args) {
//        BSTree bsTre = new BSTree();
//        int[] possAtr = {2, 4, 3, 6, 8, 7, 5};
//        Node node = bsTre.processOne(possAtr, 0, possAtr.length - 1);


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

    //递归遍历  后序序 二叉树
    public static void postCureNode(Node node) {
        if (node == null) {
            return;
        }
        postCureNode(node.left);
        postCureNode(node.right);
        printNode(node);
    }

    //打印节点
    public static void printNode(Node node) {
        if (node == null) {
            System.out.print("*");
        } else {
            System.out.print(node.data + "-");
        }
    }
}
