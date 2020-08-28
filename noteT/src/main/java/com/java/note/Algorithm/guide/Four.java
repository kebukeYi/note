package com.java.note.Algorithm.guide;

/**
 * @Author : mmy
 * @Creat Time : 2020/4/29  21:33
 * @Description 验证回文串
 */
public class Four {

    public static boolean isBackStr(String string) {
        int left = 0;
        int right = string.length() - 1;
        while (left < right) {
            char cl = string.charAt(left);
            char cr = string.charAt(right);
            if (!Character.isLetterOrDigit(cl)) {
                left++;
            } else if (!Character.isLetterOrDigit(cr)) {
                right--;
            } else {
                if (Character.toLowerCase(cl) != Character.toLowerCase(cr)) {
                    return false;
                } else {
                    left++;
                    right--;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println(isBackStr("A man, a plan, a canal: Panama"));
        System.out.println(isBackStr("race a car"));
    }
}
