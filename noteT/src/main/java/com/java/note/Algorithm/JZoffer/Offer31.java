package com.java.note.Algorithm.JZoffer;

import java.util.Stack;

/**
 * @Author : mmy
 * @Creat Time : 2020/9/11  上午 8:16
 * @Description 输入两个整数序列，第一个序列表示栈的压入顺序，请判断第二个序列是否为该栈的弹出顺序。假设压入栈的所有数字均不相等。
 * <p>
 * 例如序列 1,2,3,4,5 是某栈的压入顺序，序列 4,5,3,2,1 是该压栈序列对应的一个弹出序列，但 4,3,5,1,2 就不可能是该压栈序列的弹出序列。
 */
public class Offer31 {

    public static void main(String[] args) {
        int[] pushA = {1, 2, 3, 4, 5};
//        int[] popA = {4, 3, 5, 1, 2};
        int[] popA = {4, 5, 3, 2, 1};
        System.out.println(IsPopOrder(pushA, popA));
    }


    public static boolean IsPopOrder(int[] pushA, int[] popA) {
        Stack<Integer> stack = new Stack<>();
        int i = 0;
        for (int j = 0; j < pushA.length; j++) {
            stack.add(pushA[j]);
            while (!stack.isEmpty() && stack.peek() == popA[i]) {
                stack.pop();
                i++;
            }
            System.out.println(stack.toString());
        }
        return stack.isEmpty();
    }
}
