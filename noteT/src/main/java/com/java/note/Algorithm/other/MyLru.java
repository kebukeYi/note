package com.java.note.Algorithm.other;

import java.util.HashMap;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/9  17:28
 * @Description http://notfound9.github.io/interviewGuide/#/docs/RedisBasic
 */
public class MyLru {

    static class ListNode {
        String key;//这里存储key便于元素满时，删除尾节点时可以快速从HashMap删除键值对
        Integer value;
        ListNode pre = null;
        ListNode next = null;

        ListNode(String key, Integer value) {
            this.key = key;
            this.value = value;
        }
    }

    ListNode head;
    ListNode last;
    int limit = 4;

    HashMap<String, ListNode> hashMap = new HashMap<String, ListNode>();

    public void add(String key, Integer val) {
        ListNode existNode = hashMap.get(key);
        if (existNode != null) {

            //从链表中删除这个元素
            ListNode pre = existNode.pre;
            ListNode next = existNode.next;
            if (pre != null) {
                pre.next = next;
            }
            if (next != null) {
                next.pre = pre;
            }

            //更新尾节点
            if (last == existNode) {
                last = existNode.pre;
            }

            //移动到最前面
            head.pre = existNode;
            existNode.next = head;
            head = existNode;

            //更新值
            existNode.value = val;

        } else {
            //达到限制，先删除尾节点
            if (hashMap.size() == limit) {

                ListNode deleteNode = last;
                hashMap.remove(deleteNode.key);
                //正是因为需要删除，所以才需要每个ListNode保存key
                last = deleteNode.pre;
                deleteNode.pre = null;
                last.next = null;
            }

            ListNode node = new ListNode(key, val);
            hashMap.put(key, node);
            if (head == null) {
                head = node;
                last = node;
            } else {
                //插入头结点
                node.next = head;
                head.pre = node;
                head = node;
            }
        }

    }

    public ListNode get(String key) {
        return hashMap.get(key);
    }

    public void remove(String key) {
        ListNode deleteNode = hashMap.get(key);
        ListNode preNode = deleteNode.pre;
        ListNode nextNode = deleteNode.next;
        if (preNode != null) {
            preNode.next = nextNode;
        }
        if (nextNode != null) {
            nextNode.pre = preNode;
        }
        if (head == deleteNode) {
            head = nextNode;
        }
        if (last == deleteNode) {
            last = preNode;
        }
        hashMap.remove(key);
    }

    public static void main(String[] args) {


    }
}
