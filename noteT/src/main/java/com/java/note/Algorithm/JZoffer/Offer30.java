package com.java.note.Algorithm.JZoffer;

import java.util.Stack;

/**
 * @Author : mmy
 * @Creat Time : 2020/9/11  上午 8:16
 * @Description 定义栈的数据结构，请在该类型中实现一个能够得到栈中所含最小元素的min函数（时间复杂度应为O（1））
 */
public class Offer30 {

    public static void main(Strings[] args) {
        System.out.println();
    }

    Stack<Integer> dataStack = new Stack<>();
    Stack<Integer> minStack = new Stack<>();


    public void push(int node) {
        dataStack.push(node);
        if (minStack.isEmpty() || minStack.peek() >= node)
            minStack.push(node);
    }

    public void pop() {
        if (dataStack.pop().equals(minStack.peek()))
            minStack.pop();

    }

    public int top() {
        return dataStack.peek();
    }

    public int min() {
        return minStack.peek();
    }
}
