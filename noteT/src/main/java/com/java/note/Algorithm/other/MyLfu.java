package com.java.note.Algorithm.other;

import lombok.Data;

import java.util.HashMap;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/9  17:31
 * @Description 访问频率
 */
public class MyLfu {
    @Data
    static class ListNode {
        String key;
        String value;
        int hz;
        ListNode pre;
        ListNode next;

        public ListNode(String key, String value, int hz) {
            this.key = key;
            this.value = value;
            this.hz = hz;
        }
    }

    int limit = 15;
    int hz = 1;
    HashMap<String, ListNode> hashMap = new HashMap<>(limit);
    ListNode head;
    ListNode tail;


    public void add(String key, String value) {
        if (limit == 15) {
            if (hashMap.containsKey(key)) {
                ListNode node = hashMap.get(key);
                node.setValue(value);
            } else {
                //删除最后一个节点
                ListNode last = tail.pre;
                tail = last;
                tail.next = null;


                //增加一个前节点
                ListNode node = new ListNode(key, value, hz);
                node.next = head;
                node.pre = null;
                head.pre = node;


            }

        } else {
            if (hashMap.containsKey(key)) {
                ListNode node = hashMap.get(key);
                node.setValue(value);
            } else {
                //删除最后一个节点


                //增加一个前节点

            }
        }

    }

    public ListNode getListNode(String key) {
        if (hashMap.containsKey(key)) {
            //删除连中的节点
            ListNode node = hashMap.get(key);
            if (node != head) {
                ListNode pre = node.pre;
                node.next.pre = pre;
            } else if (node == head) {
                return hashMap.get(key);
            }

            if (node != tail) {
                ListNode post = node.next;
                node.pre.next = post;
            } else if (node == tail) {

            }

            //放到头节点
            head.pre = node;
            node.pre = null;
            node.next = head;
            head = node;


        }
        return null;
    }

    public void removeListNode(String key) {


    }


}
