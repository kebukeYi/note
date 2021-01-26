package com.java.note.Algorithm.other;

import java.util.ArrayList;

/**
 * @Author : fang.com
 * @CreatTime : 2021-01-08 22:04
 * @Description :
 * @Version :  0.0.1
 */
public class DEMO {

    public static void main(String[] args) {
        int num = 1;
        int i = 0;
        ArrayList<Integer> list = new ArrayList<>();
        for (i = 1; i <= 100; i++) {
            list.add(i);
        }
        i = 0;
        while (true) {
            if (i == list.size()) {
                i = 0;
            }
            if (1 == list.size()) {
                System.out.println(list.get(0));
                break;
            }
            if (num % 14 == 0) {
                list.remove(i);
                i = i - 1;
            }
            i++;
            num++;
        }
    }
}
