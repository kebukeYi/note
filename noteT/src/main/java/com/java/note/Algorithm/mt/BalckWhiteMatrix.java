package com.java.note.Algorithm.mt;

import java.util.*;

/**
 * @Author : mmy
 * @Creat Time : 2020/4/14  16:06
 * @Description 黑白矩阵
 * 计算奇 偶 格子中每个数出现的频率 并且把频率排序 采用 TreeMap 和entry 排序
 * 然后 一一判断
 */
public class BalckWhiteMatrix {

    public static void main(String[] args) {
        int m = 0;//列数
        int n = 0;//行数
        Scanner scanner = new Scanner(System.in);
        n = scanner.nextInt();
        m = scanner.nextInt();
        int[][] bt = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                bt[i][j] = scanner.nextInt();
            }
        }
        int key = 0;
        int value = 0;
        int result = 0;
        TreeMap<Integer, Integer> as = new TreeMap<>();
        TreeMap<Integer, Integer> bs = new TreeMap<>();

        for (int i = 0; i < bt.length; i++) {
            for (int j = 0; j < bt[0].length; j++) {
                if ((i + j) % 2 == 0) {
                    key = bt[i][j];
                    if (as.containsKey(key)) {
                        value = as.get(key);
                        as.put(key, ++value);
                    } else {
                        as.put(key, 0);
                    }
                } else {
                    if (bs.containsKey(key)) {
                        value = bs.get(key);
                        bs.put(key, ++value);
                    } else {
                        bs.put(key, 0);
                    }
                }
            }
        }

        List<Map.Entry<Integer, Integer>> asMapList = new ArrayList<>(as.entrySet());
        asMapList.sort((o1, o2) -> o2.getValue() - o1.getValue());
        List<Map.Entry<Integer, Integer>> bsMapList = new ArrayList<>(bs.entrySet());
        bsMapList.sort((o1, o2) -> o2.getValue() - o1.getValue());

        if (!asMapList.get(0).getKey().equals(bsMapList.get(0).getKey())) {
            result = n * m - asMapList.get(0).getValue() - bsMapList.get(0).getValue();
        } else {
            if (asMapList.get(0).getValue() > bsMapList.get(0).getValue()) {
                result = n * m - asMapList.get(0).getValue() - bsMapList.get(1).getValue();
            } else {
                result = n * m - asMapList.get(1).getValue() - bsMapList.get(0).getValue();
            }
        }
        System.out.println(result);
    }
}
