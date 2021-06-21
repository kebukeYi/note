package com.java.note.Algorithm.JZoffer;

import java.util.Stack;

/**
 * @Author : mmy
 * @Creat Time : 2020/9/10  上午 8:32
 * @Description 用两个栈来实现一个队列，完成队列的 Push 和 Pop 操作。
 * FIFO
 */
public class Offer09 {

    public static void main(Strings[] args) {

    }

    Stack<Integer> stack1 = new Stack<Integer>();
    Stack<Integer> stack2 = new Stack<Integer>();

    public Integer push(Integer integer) {
        return stack1.push(integer);
    }

    public Integer pop() {
        if (stack2.isEmpty()) {
            return null;
        }
        while (!stack1.isEmpty()) {
            stack2.push(stack1.pop());
        }
        return stack2.pop();
    }


}
