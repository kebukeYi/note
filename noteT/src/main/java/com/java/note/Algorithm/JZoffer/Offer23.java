package com.java.note.Algorithm.JZoffer;

/**
 * @Author : mmy
 * @Creat Time : 20 20/9/11  上午 8:16
 * @Description 一个链表中包含环，请找出该链表的环的入口结点。要求不能使用额外的空间。
 */
public class Offer23 {

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

    //https://www.bilibili.com/video/BV1ZK4y1b7Xn?p=21
    public static ListNode get(ListNode head) {
        //双指针 快慢指针
        ListNode slow = head;
        ListNode fast = head;

        while (fast != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) {
                slow = head;
                while (slow != fast) {
                    slow = slow.next;
                    fast = fast.next;
                }
                return slow;
            }
        }
        return null;
    }
}
