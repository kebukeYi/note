package com.java.note.Algorithm.leecode;

import java.util.HashMap;

/**
 * @Author : mmy
 * @Creat Time : 2020/4/24  14:29
 * @Description
 */
public class LRUCache146 {

    HashMap<Integer, ListNode> hashMap = new HashMap<>();
    ListNode head;
    ListNode tail;
    int count;
    int capacity;

    class ListNode {
        int key;
        int value;
        ListNode pre;
        ListNode next;
    }

    public LRUCache146(int capacity) {
        this.count = 0;
        this.capacity = capacity;
        head = new ListNode();
        head.pre = null;

        tail = new ListNode();
        tail.next = null;

        head.next = tail;
        tail.pre = head;

    }

    public void moveToHead(ListNode node) {
        removeNode(node);
        addNode(node);
    }

    public void removeNode(ListNode node) {
        ListNode pre = node.pre;
        ListNode next = node.next;

        pre.next = next;
        next.pre = pre;
    }

    public void addNode(ListNode node) {
        node.pre = head;
        node.next = head.next;
        head.next.pre = node;
        head.next = node;
    }

    public int get(int key) {
        ListNode node;
        if (hashMap.containsKey(key)) {
            node = hashMap.get(key);
            moveToHead(node);
            return node.value;
        } else {
            return -1;
        }
    }

    private ListNode popTailNode() {
        ListNode res = tail.pre;
        removeNode(res);
        return res;
    }

    public void put(int key, int value) {
        ListNode node = hashMap.get(key);
        if (node != null) {// 命中 更新
            node.value = value;
            moveToHead(node);
        } else {
            ListNode newnode = new ListNode();
            newnode.key = key;
            newnode.value = value;
            ++count;
            hashMap.put(key, newnode);
            addNode(newnode);
            if (count > capacity) {
                ListNode tail = this.popTailNode();
                hashMap.remove(tail.key);
                count--;
            }
        }

    }

}
