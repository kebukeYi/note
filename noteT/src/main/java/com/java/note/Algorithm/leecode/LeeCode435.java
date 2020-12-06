package com.java.note.Algorithm.leecode;

import java.util.Arrays;
import java.util.Comparator;

/**
 * @Author : mmy
 * @Creat Time : 2020/10/8  下午 9:20
 * @Description 给定一个区间的集合，找到需要移除区间的最小数量，使剩余区间互不重叠。
 */
public class LeeCode435 {


    /**
     * 输入: [ [1,2], [2,3], [3,4], [1,3] ]
     * 输出: 1
     * 解释: 移除 [1,3] 后，剩下的区间没有重叠
     */

    //终点贪心解法： 按区间的结尾进行升序排序，每次选择结尾最小，并且和前一个区间不重叠的区间。
    public int eraseOverlapIntervals(int[][] intervals) {
        if (intervals.length == 0) {
            return 0;
        }
        //根据末尾元素进行排序
        Arrays.sort(intervals, new Comparator<int[]>() {
            @Override
            public int compare(int[] t1, int[] t2) {
                //升序排列
                return t1[1] - t2[1];
            }
        });

        //最多能组成的不重叠区间个数   留下的数量
        int count = 1;

        int end = intervals[0][1];

        for (int i = 1; i < intervals[0].length; i++) {

            //找下一个起点大于前一个终点的元素

            //没有就下一个
            if (intervals[i][0] < end) {
                continue;
            }

            //有 就更新end值 然后下一个
            end = intervals[i][1];
            count++;

        }

        return intervals.length - count;
    }


    public int eraseBeginlapIntervals(int[][] intervals) {

        return 0;
    }


}
