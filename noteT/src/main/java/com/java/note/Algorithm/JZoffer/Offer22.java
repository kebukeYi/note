package com.java.note.Algorithm.JZoffer;

/**
 * @Author : mmy
 * @Creat Time : 2020/9/11  上午 8:16
 * @Description 输入一个链表，输出该链表中倒数第k个结点。
 */
public class Offer22 {

    class ListNode {
        int val;
        ListNode next = null;

        ListNode(int val) {
            this.val = val;
        }

    }

    public static void main(String[] args) {
        System.out.println();
    }

    /**
     * 设链表的长度为 N。设置两个指针 P1 和 P2，先让 P1 移动 K 个节点，则还有 N - K 个节点可以移动。
     * 此时让 P1 和 P2 同时移动，可以知道当 P1 移动到链表结尾时，P2 移动到第 N - K 个节点处，该位置就是倒数第 K 个节点。
     */
    public ListNode FindKthToTail(ListNode head, int k) {
        //双指针
        ListNode first = head;
        ListNode second = head;
        for (int i = 0; i < k; i++) {
            if (first == null) return null;
            first = first.next;
        }

        while (first != null) {
            first = first.next;
            second = second.next;
        }
        return second;
    }


    public static int get(int n) {

        return n;
    }
}
