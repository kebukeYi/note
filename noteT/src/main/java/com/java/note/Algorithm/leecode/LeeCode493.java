package com.java.note.Algorithm.leecode;

import lombok.Data;

/**
 * @Author : mmy
 * @Creat Time : 2020/4/24  15:26
 * @Description 翻转对给定一个数组 nums ，如果 i < j 且 nums[i] > 2*nums[j] 我们就将 (i, j) 称作一个重要翻转对。
 * <p>
 * 你需要返回给定数组中的重要翻转对的数量。
 * <p>
 * 链接：https://leetcode-cn.com/problems/reverse-pairs
 */
public class LeeCode493 {

    public int reversePairs(int[] nums) {

        return 1;
    }

    @Data
    class Node {
        private Node next;
        private Integer value;
    }


    public Node trancforLinked(Node root) {
        if (root == null) return null;
        if (root.next == null) {
            return root;
        }
        //1->2->3->4
        //1<-2<-3<-4
        while (root.next != null) {
            Node cur = root;
            final Node second = root.next;
            final Node third = root.next.next;
            cur = second.next;
            root = second;
        }

        return root;
    }
}
