package com.java.note.Algorithm.JZoffer;

import lombok.Data;

/**
 * @Author : mmy
 * @Creat Time : 2020/9/11  上午 8:16
 * @Description 输入一个复杂链表（每个节点中有节点值，以及两个指针，一个指向下一个节点，另一个特殊指针指向任意一个节点），
 * 返回结果为复制后复杂链表的 head。
 * 请对此链表进行深拷贝，并返回拷贝后的头结点。（注意，输出结果中请不要返回参数中的节点引用，否则判题程序会直接返回空）
 */
public class Offer35 {

    @Data
    static
    class RandomListNode {
        int label;
        RandomListNode next = null;
        RandomListNode random = null;

        RandomListNode(int label) {
            this.label = label;
        }
    }


    public static void main(String[] args) {
        System.out.println();
    }

    /**
     * 1. 复制节点在之后
     * 2. 复制随机节点
     * 3. 进行链表拆开
     */
    public static RandomListNode get(RandomListNode randomListNode) {
        if (randomListNode == null) return randomListNode;

        copy(randomListNode);
        copyRandom(randomListNode);
        return divied(randomListNode);
    }

    private static RandomListNode divied(RandomListNode head) {
        //7`
        RandomListNode copyNode = head.next;

        // 7`
        RandomListNode copyHead = copyNode;

        //7  -  8
        head.next = copyNode.next;
        //8
        head = head.next;
        while (head != null) {
            //7` -  8`
            copyHead.next = head.next;
            //7 - 8 - 9
            head.next = head.next.next;
            //9
            head = head.next;
            // 8`
            copyHead = copyNode.next;
        }


        return copyHead;
    }

    private static void copyRandom(RandomListNode randomListNode) {
        while (randomListNode != null) {
            //下一个复制的节点
            RandomListNode copyNext = randomListNode.next;
            if (randomListNode.random != null) {
                //找到当前节点的 random 节点
                RandomListNode random = randomListNode.random;
                //赋值给 random 节点的下一个复制的 random 节点
                copyNext.random = random.next;
            }
            randomListNode = randomListNode.next;
        }

    }

    private static void copy(RandomListNode curListNode) {
        while (curListNode != null) {
            //可能是 2  或者 null
            RandomListNode rightNext = curListNode.next;
            //复制节点
            RandomListNode copyNext = new RandomListNode(curListNode.getLabel());
            //赋值 复制节点
            curListNode.next = copyNext;
            //复制节点 需要接入链表中
            copyNext.next = rightNext;

            curListNode = copyNext.next;
        }
    }

}
