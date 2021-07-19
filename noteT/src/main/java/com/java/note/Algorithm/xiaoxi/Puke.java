package com.java.note.Algorithm.xiaoxi;

import javax.validation.constraints.Min;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author : kebukeyi
 * @date :  2021-07-19 09:22
 * @description: 从扑克牌中随机抽5张牌，判断是不是一个顺子，即这5张牌是不是连续的。
 * 2～10为数字本身，A为1，J为11，Q为12，K为13，而大、小王为 0 ，可以看成任意数字。A 不能视为 14
 * @question:
 * @link:
 **/
public class Puke {


    //最大值 -最小值 <=4
    //有对子就不是顺子
    public static boolean isOrder(int[] array) {
        Set<Integer> set = new HashSet<>();
        int max = 0;
        int min = 14;
        for (int i : array) {
            if (i == 0) continue;
            max = Math.max(i, max);
            min = Math.min(i, min);
            if (!set.add(i)) {
                return false;
            }
        }
        return max - min < 5;
    }

    public static void main(String[] args) {
        int[] array = new int[]{1, 0, 5, 4, 5};
        System.out.println(isOrder(array));
    }

}
 
