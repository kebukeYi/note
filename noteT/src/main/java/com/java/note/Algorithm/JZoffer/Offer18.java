package com.java.note.Algorithm.JZoffer;

/**
 * @Author : mmy
 * @Creat Time : 2020/9/10  下午 3:00
 * @Description 返回删除后的链表的头节点。
 */
public class Offer18 {

    public class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }
    }

    public static void main(Strings[] args) {
    }

    //删除链表节点
    public ListNode deleteNode(ListNode head, int val) {
        //保存当前节点 为的是 指向下一个节点

        if (head.val == val) {
            return head.next;
        }

        ListNode per = head;
        ListNode cur = head.next;

        while (cur.val != val && cur != null) {
            per = cur;
            cur = cur.next;
        }

        if (cur != null) {
            per.next = cur.next;
        }

        return head;
    }

    //删除链表重复元素
    public ListNode deleteDuplication(ListNode head, int val) {
        return null;
    }

    public ListNode deleteDuplication(ListNode pHead) {
        if (pHead == null || pHead.next == null) {
            return pHead;
        }
        //新建节点
        ListNode Head = new ListNode(0);
        //新建节点 指向 头节点
        Head.next = pHead;
        //新建节点指针
        ListNode pre = Head;
        //头指针
        ListNode last = Head.next;
        while (last != null) {
            //头节点的值跟头节点的下一个值相同的话
            if (last.next != null && last.val == last.next.val) {
                // 找到最后的一个相同节点
                while (last.next != null && last.val == last.next.val) {
                    last = last.next;
                }
                //新建指针的下一个节点是不重复的节点
                pre.next = last.next;
                //指向下一个节点
                last = last.next;
            } else {
                //如果开头没有出现重复节点
                //就逐一递增一个节点
                pre = pre.next;
                last = last.next;
            }
        }
        return Head.next;
    }

}
