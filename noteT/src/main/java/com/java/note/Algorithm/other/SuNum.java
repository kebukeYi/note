package com.java.note.Algorithm.other;

import java.util.Arrays;

/**
 * @Author : mmy
 * @Creat Time : 2020/10/8  下午 2:10
 * @Description 快速找计算素数
 */
public class SuNum {


    public static int getSuNumbers(int num) {
        int count = 0;
        for (int i = 2; i < num; i++) {
            int flag = 0;
            // for (int j = 2; j <i; j++) {  //开始做法
            //根号做法
            for (int j = 2; j * j <= i; j++) {

                if (i % j == 0) {
                    flag = 1;
                    break;
                }
            }
            if (flag == 0) {
                count++;
            }

        }

        return count;
    }

    public static int getSuNumber(int num) {
        int count = 0;
        boolean[] isPerm = new boolean[num];
        Arrays.fill(isPerm, true);

        for (int i = 2; i < num; i++) {
            if (isPerm[i]) {
                for (int j = i * 2; j < num; j = j + i) {
                    isPerm[j] = false;
                }
            }
        }
        for (int i = 2; i < num; i++) {
            if (isPerm[i]) {
                count++;
            }
        }

        return count;
    }

    public static void main(String[] args) {
        System.out.println(getSuNumbers(20000));
        System.out.println(getSuNumber(20000));
    }

}
