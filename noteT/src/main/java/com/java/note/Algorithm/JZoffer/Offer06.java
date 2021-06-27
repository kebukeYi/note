package com.java.note.Algorithm.JZoffer;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author : mmy
 * @Creat Time : 2020/4/16  14:42
 * @Description 输入一个链表的头节点，从尾到头反过来返回每个节点的值（用数组返回）
 * 1.利用递归的回溯
 * 2.利用辅助栈 （先进后出）
 * 3.头插法
 */
public class Offer06 {

    public class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }
    }

    public static void main(String[] args) {

    }

    static List<Integer> list = new ArrayList<>();

    public int[] reversePrint(ListNode head) {
        if (head == null) return new int[]{};
        recur(head);
        int[] result = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
        }
        return result;
    }

    //    回溯时执行 list.add(head.val);
    public static void recur(ListNode node) {
        if (node == null) {
            return;
        }
        recur(node.next);
        list.add(node.val);
    }


}
