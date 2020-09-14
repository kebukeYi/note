package com.java.note.Algorithm.JZoffer;

/**
 * @Author : mmy
 * @Creat Time : 2020/9/10  下午 3:00
 * @Description 请实现一个函数用来判断字符串是否表示数值（包括整数和小数）。
 * 例如，字符串"+100","5e2","-123","3.1416"和"-1E-16"都表示数值。
 * 但是"12e","1a3.14","1.2.3","+-5"和"12e+4.3"都不是。
 */
public class Offer20 {
    public static void main(String[] args) {
        System.out.println(get("2"));
    }


    public static boolean get(String h) {


        return false;
    }

    public boolean isNumeric(char[] str) {
        if (str == null || str.length == 0)
            return false;
        return new String(str).matches("[+-]?\\d*(\\.\\d+)?([eE][+-]?\\d+)?");
    }

}
