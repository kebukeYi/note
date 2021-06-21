package com.java.note.Algorithm.JZoffer;

/**
 * @Author : mmy
 * @Creat Time : 2020/9/11  上午 8:16
 * @Description 反转链表
 */
public class Offer24 {

    class ListNode {
        int val;
        ListNode next = null;

        ListNode(int val) {
            this.val = val;
        }
    }

    public static void main(Strings[] args) {
        System.out.println();
    }


    public ListNode ReverseList1(ListNode head) {
        if (head.next != null) {
            ListNode pre = head;
            ListNode res = ReverseList(head.next);
            res.next = pre;
        }
        return head;
    }

    public ListNode ReverseList(ListNode head) {
        if (head == null || head.next == null)
            return head;

        ListNode next = head.next;
        head.next = null;

        ListNode newHead = ReverseList(next);

        next.next = head;

        return newHead;
    }

    public ListNode ReverseList3(ListNode head) {
        ListNode pre = null;
        ListNode cur = head;
        ListNode temp = null;

        while (cur != null) {
            temp = cur.next;
            cur.next = pre;
            pre = cur;
            cur = temp;
        }
        return pre;
    }

}
