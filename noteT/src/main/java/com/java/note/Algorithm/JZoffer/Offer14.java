package com.java.note.Algorithm.JZoffer;

/**
 * @Author : mmy
 * @Creat Time : 2020/9/10  上午 8:51
 * @Description 把一根绳子剪成多段，并且使得每段的长度乘积最大。
 */
public class Offer14 {

    public static void main(Strings[] args) {
        System.out.println(get(10));
    }

    /**
     * 解题思路，找出最优解的规律:
     * 当target等于1，2，3 的时候，结果是固定的
     * 当target大于3的时候，可以看以下数据
     * target=4, 最优解：2 2
     * target=5, 最优解：3 2
     * target=6, 最优解：3 3
     * target=7, 最优解：3 2 2
     * target=8, 最优解：3 3 2
     * target=9, 最优解：3 3 3
     * target=10,最优解：3 3 2 2
     * target=11,最优解：3 3 3 2
     * target=12,最优解：3 3 3 3
     * target=13,最优解：3 3 3 2 2
     * target=14,最优解：3 3 3 3 2
     * target=15,最优解：3 3 3 3 3
     * <p>
     * 所以不难发现3和2的个数规律
     * 尽可能多剪长度为 3 的绳子，并且不允许有长度为 1 的绳子出现。如果出现了，
     * 就从已经切好长度为 3 的绳子中拿出一段与长度为 1 的绳子重新组合，把它们切成两段长度为 2 的绳子;
     */
    public static int get(int target) {
        if (target < 2) return 0;
        if (target == 2) return 1;
        if (target == 3) return 2;

        int of3 = target / 3;
        if (target - of3 * 3 == 1) {
            of3--;
        }

        int of2 = (target - of3 * 3) / 2;

        return (int) (Math.pow(3, of3) * Math.pow(2, of2));
    }


}
