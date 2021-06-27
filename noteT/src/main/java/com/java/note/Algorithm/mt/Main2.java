package com.java.note.Algorithm.mt;


import java.util.Scanner;

/**
 * @Author : mmy
 * @Creat Time : 2020/4/23  20:14
 * @Description 1.货币数值的整数部分要求每3位加一个英文逗号','（不含引号）。例如12345678应该规范化为12,345,678
 * 2.货币数值最多只有两位小数，如果有多余的小数位数应当舍去。注意，不是四舍五入。
 * 3.负数代表欠款，在规范化后应当在数值两端加上括号 '(' 和 ')' ，然后省略掉负号。
 * 4.应当在数值前面，前括号后面（如果有括号的话）加上金钱符号'$'（不含引号）
 * <p>
 * 203323      2 0 3  , 3 2 3
 * 0.0
 * 0.000000
 * 0.009212121
 * 343444323.32432
 * -12344.1
 * -12345678.9
 * <p>
 * $203,323.00
 * $0.00
 * $0.00
 * $0.00
 * $343,444,323.32
 * ($12,344.10)
 * ($12,345,678.90)
 */
public class Main2 {

    final static String $ = "$";

    public static void main(String[] args) {
        String str = null;
        do {
            Scanner s = new Scanner(System.in);
//            System.out.println("请输入：");
            str = s.nextLine().trim();
            if (str.equals("over")) {
                break;
            }
//            System.out.println("正在计算...");
            String[] bt = str.split("\\.");
//            System.out.println(bt[0]);
//            System.out.println(bt[1]);
            int a = Integer.parseInt(bt[0]);
            String b = bt[1];
            if (!b.equals("") && b != null) {
                if (b.length() > 2) {
                    b = b.substring(0, 2);
                } else if (b.length() == 0) {

                } else if (b.length() == 1) {
                    b = b + "0";
                }
            }

            char[] chars = bt[0].toCharArray();
            int length = chars.length - 1;
            int count = 0;
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = length; i >= 0; i--) {
                stringBuilder.insert(0, chars[i]);
                count++;
                if (count % 3 == 0 && count != length + 1) {
                    stringBuilder.insert(0, ",");
                }
            }

            stringBuilder.insert(0, $);
            stringBuilder.append(".");
            stringBuilder.append(b);

            if (a >= 0) {
            } else {
                stringBuilder.replace(1, 3, "");
                stringBuilder.insert(0, "(");
                stringBuilder.append(')');
            }
            System.out.println(stringBuilder.toString());
        } while (1 == 1);


    }
}
