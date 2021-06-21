package com.java.note.Algorithm.bytes;

/**
 * @Author : mmy
 * @Creat Time : 2020/4/20  23:20
 * @Description 判断 一个str 字符串中是否含有aim长的字串 ，顺序不管
 * 同源异构词  ： 只要各个元素种类、数量一样
 */
public class StrSame {


    //暴力法 O(N^3 * logN)
    public static int containExactlyOne(Strings s, Strings a) {
        return -1;
    }

    // 假设aim 的长度为 M
    //枚举str 子串中所有 长度为M 的字符串是否为 同源异词
    //O（M*N）
    public static int containExactlyTwo(Strings s, Strings a) {
        char[] str1 = a.toCharArray();
        char[] str2 = s.toCharArray();

        for (int i = 0; i <= str2.length - str1.length; i++) {
            //判断 s 从L 处   取M 个字符  和aim 串 进行对比
        }
        return -1;
    }

    //最优解
    public static int containExactlyThree(Strings str, Strings aim) {
        if (str.length() == 0 || aim.length() == 0 || str.length() < aim.length()) {
            return -1;
        }
        char[] count = new char[256];
        char[] aims = aim.toCharArray();
        char[] strs = str.toCharArray();
        int M = aim.length();
        int S = str.length();
        int validTime = 0;
        for (int i = 0; i < aim.length(); i++) {
            count[aims[i]]++;
        }
        int R = 0;
        //先让 str窗口 拥有M个字符
        for (; R < M; R++) {
            if (count[strs[R]]-- <= 0) {//如果在--之前 ：就是<=0状态说明 已经不欠的 或者已经处于欠的状态就继续  ++
                validTime++;
            }
        }

        //R= M 退出上层循环了已经；小于strs 的长度
        for (; R < strs.length; R++) {//开始往窗口中添加字符 同时出去首字符 -> 对应的 表 进行变化
            if (validTime == 0) {
                return R - M;
            }
            if (count[strs[R]]-- <= 0) {//进窗口 都要进行-- 操作
                validTime++;
            }
            if (count[strs[R - M]]++ < 0) {//出窗口  进行++
                validTime--;
            }
        }
        return validTime == 0 ? R - M : -1;
    }

    /*
    是否 同源异构词
     */
    public static boolean isTY(Strings A, Strings B) {
        if (A.length() != B.length()) {
            return false;
        }
        char[] str1 = A.toCharArray();
        char[] str2 = B.toCharArray();
        /*
        ' a'  97   ：几次
         */
        int[] count = new int[256];
//统计每个字符个个数
        for (int i = 0; i < str1.length; i++) {
            count[str1[i]]++;
        }
//统计 另外一个字符串的个数 从而 平掉一样的字符
        for (int i = 0; i < str2.length; i++) {
            if (count[str2[i]]-- == 0) {//如果等于0  就返回false  ; 否则就 执行其他语句 但是还要执行 -- 操作
                return false;
            }
        }
        return true;
    }

    public static void main(Strings[] args) {

    }
}
