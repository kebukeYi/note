package com.java.note.Algorithm.mianshi;

import java.util.Arrays;
import java.util.Comparator;
import java.util.TreeMap;

/**
 * @Author : mmy
 * @Creat Time : 2020/9/16  下午 5:28
 * @Description 小明的女朋友最喜欢在网上买买买了，可是钱包里钞票有限，不能想买啥就买啥。面对琳琅满目的物品，
 * 她想买尽可能多的种类，每种只买一件，同时总价格还不能超过预算上限。于是她请小明写程序帮她找出应该买哪些物品，并算出这些物品的总价格。
 * <p>
 * 输入规范：
 * 每个输入包含两行。第一行是预算上限。第二行是用空格分隔的一组数字，代表每种物品的价格。所有数字都为正整数并且不会超过10000。
 * <p>
 * 输出规范：
 * 对每个输入，输出应买物品的总价格。
 * <p>
 * 输入示例1:
 * 100
 * 50 50
 * 输出示例1:
 * 100
 * <p>
 * 输入示例2:
 * 188
 * 50 42 9 15 105 63 14 30
 * 输出示例2:
 * 160
 */
public class Hong {

    public static void main(String[] args) {
        TreeMap<Integer, Integer> map1 = new TreeMap<Integer, Integer>();  //默认的TreeMap升序排列
        TreeMap<Integer, Integer> map2 = new TreeMap<Integer, Integer>(new Comparator<Integer>() {
            /*
             * int compare(Object o1, Object o2) 返回一个基本类型的整型，
             * 返回负数表示：o1 小于o2，
             * 返回0 表示：o1和o2相等，
             * 返回正数表示：o1大于o2。
             */
            @Override
            public int compare(Integer a, Integer b) {
                return b - a;
            }
        });
        map2.put(1, 2);
        map2.put(2, 4);
        map2.put(7, 1);
        map2.put(5, 2);
        System.out.println("Map2=" + map2);

        map1.put(2, 2);
        map1.put(8, 4);
        map1.put(7, 1);
        map1.put(5, 2);
        System.out.println("map1=" + map1);
    }

    public static void main1(String[] args) {


        System.out.println(1 % 3);


        //商品价格
        Integer[] ints = {50, 42, 9, 15, 105, 63, 14, 30};
        //预算上限
        Integer sum = 188;
        Integer num = calculate(ints, sum);
        System.out.println(num);
    }

    public static Integer calculate(Integer[] ints, Integer sum) {
        Integer count = 0;
        Integer value;
        Arrays.sort(ints);
        for (Integer integer : ints) {
            value = count;
            count += integer;
            if (count > sum) {
                return value;
            }
        }
        return count;
    }

}
