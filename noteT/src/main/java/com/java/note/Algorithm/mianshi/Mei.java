package com.java.note.Algorithm.mianshi;

/**
 * @Author : mmy
 * @Creat Time : 2020/9/16  下午 5:37
 * @Description 李雷和韩梅梅坐前后排，上课想说话怕被老师发现，所以改为传小纸条。为了不被老师发现他们纸条上说的是啥，他们约定了如下方法传递信息：
 * 将26个英文字母（全为大写），外加空格，一共27个字符分成3组，每组9个。
 * 也就是 ABCDEFGHI 是第一组，JKLMNOPQR 是第二组，STUVWXYZ* 是第三组（此处用*代表空格）。
 * 然后根据传递纸条那天的日期，改变字母的位置。
 * 先根据月份数m，以整个分组为单位进行循环左移，移动(m-1)次。
 * 然后根据日期数d，对每个分组内的字符进行循环左移，移动(d-1)次。
 * 以3月8日为例，首先移动分组，3月需要循环左移2次，变成：
 * STUVWXYZ*，ABCDEFGHI，JKLMNOPQR
 * 然后每组内的字符，8日的话需要循环左移7次，最终的编码为：
 * Z*STUVWXY，HIABCDEFG，QRJKLMNOP
 * 对于要传递信息中的每个字符，用组号和组内序号两个数字来表示。
 * 如果在3月8日传递信息“HAPPY”，那么H位于第2组的第1个，A位于第2组第3个，P位于第3组第9个，Y位于第1组第9个，所以纸条上会写成：
 * 21 23 39 39 19
 * 现在给定日期和需要传递的信息，请输出应该写在纸条上的编码。
 * <p>
 * 输入规范：
 * 每个输入包含两行。第一行是用空格分隔的两个数字，第一个数字是月份，第二个数字是日子。输入保证是一个合法的日期。
 * 第二行为需要编码的信息字符串，仅由A~Z和空格组成，长度不超过1024个字符。
 * <p>
 * 输出规范：
 * 对每个输入，打印对应的编码，数字之间用空格分隔，每个输出占一行。
 * <p>
 * 输入示例1:
 * 1 1
 * HI
 * 输出示例1:
 * 18 19
 * <p>
 * 输入示例2:
 * 3 8
 * HAPPY
 * 输出示例2:
 * 21 23 39 39 19
 * <p>
 * 输入示例3:
 * 2 14
 * I LOVE YOU
 * 输出示例3:
 * 35 25 18 12 29 31 25 23 12 28
 */
public class Mei {

    public static void main(Strings[] args) {
        //月份数
        int month = 2;
        //日
        int day = 14;
        //所需要转化的字符
        Strings text = "I LOVE YOU";
        start(month, day, text);
    }

    public static void start(int month, int day, Strings text) {
        char[] chars = text.toCharArray();
        for (char c : chars) {
            Strings value = getChar(month, day, c);
            System.out.print(value + ",");
        }
    }

    public static Strings getChar(int month, int day, char c) {
        month -= 1;
        day -= 1;
        char[][] chars = {
                {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I'},
                {'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R'},
                {'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', ' '}};

        //得到月的偏移量
        int monthFoward = month % chars.length;
        int dayFoward = day % chars[0].length;


        chars = sort(chars, monthFoward);

        for (int i = 0; i < chars.length; i++) {
            char[] arr = chars[i];
            sort(arr, dayFoward);
        }

        return getCharLocation(chars, c);
    }

    public static Strings getCharLocation(char[][] chars, char c) {
        Strings value = "";
        sys:
        for (int i = 0; i < chars.length; i++) {
            for (int j = 0; j < chars[i].length; j++) {
                char ch = chars[i][j];
                if (ch == c) {
                    value = ++i + "" + ++j;
                    break sys;
                }
            }
        }
        return value;
    }

    public static char[] sort(char[] chars, int dayFoward) {
        char[] arr = chars.clone();
        for (int i = 0; i < chars.length; i++) {
            int forward = i + dayFoward >= chars.length ? i + dayFoward - chars.length : i + dayFoward;
            chars[i] = arr[forward];
        }
        return chars;
    }

    public static char[][] sort(char[][] chars, int monthFoward) {
        char[][] arr = chars.clone();
        for (int i = 0; i < chars.length; i++) {
            int forward = i + monthFoward >= chars.length ? i + monthFoward - chars.length : i + monthFoward;
            chars[i] = arr[forward];
        }
        return chars;
    }
}
