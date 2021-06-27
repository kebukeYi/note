package com.java.note.Algorithm.mt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * @Author : mmy
 * @Creat Time : 2020/4/23  20:58
 * @Description 目前，你和你的家里人一共k个人一起去买生活用品。由于打折活动力度很大，每个人只能去付款一次，但是这一次买的东西价格是不做限制的。
 * 熊爷爷的超市物品分为两类：
 * A类和B类物品，活动是如果一个人买的商品中含有A类物品，那么他买的所有物品中最便宜的一件物品半价。
 * 如果一个人买的商品中只有B类物品，那么他买的物品不打折。
 * 你们计划要买n个物品，现在将这n个物品的信息给你，请你计算如何分配k位家人比较合算。
 * 第一行有两个整数n,k，代表物品的数量和人的数量。
 * 接下来n行，每行两个整数u,v描述一个物品。u代表物品的价格，v代表商品的种类。如果v为1，代表其为A类商品。如果v为2，代表其为B类商品。
 * 1<=n,k<=1000,1<=u<=1000000，v∈{1,2}
 * 8 5
 * 10 1
 * 8 2
 * 6 2
 * 4 1
 * 6 1
 * 4 7
 */
public class Main3 {

    public static void main(String[] args) {
        HashMap<Integer, List<Integer>> shop = new HashMap<>();// 行 列list
        List<Integer> priceList = null;
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int k = scanner.nextInt();
        List<Integer> cols = null;//每行 的列list

        for (int i = 0; i < n; i++) {
            int x = scanner.nextInt();//价格
            String s = scanner.nextLine().trim();
            int y = Integer.parseInt(s);//种类
            priceList = shop.get(y);
            if (priceList == null) {
                priceList = new ArrayList<>();
                priceList.add(x);
                shop.put(y, priceList);
            } else {
                priceList.add(x);
                shop.put(y, priceList);
            }

        }

    }
}
