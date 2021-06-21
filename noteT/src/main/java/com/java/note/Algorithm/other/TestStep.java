package com.java.note.Algorithm.other;

/**
 * @Author : mmy
 * @Creat Time : 2020/7/14  18:09
 * @Description 上台阶
 */
public class TestStep {

    public static int f(int n) {
        if (n < 1) throw new IllegalArgumentException(n + "不能小于1");
        if (n == 1 || n == 2) return n;
        //有很多资源浪费
        //中途计算结果没有进行保存
        return f(n - 1) + f(n - 2);
    }


    //f(n)=f(n-1)+f(n-2)
    public static int loop(int n) {
        if (n < 1) throw new IllegalArgumentException(n + "不能小于1");
        if (n == 1 || n == 2) return n;
        // 保存 最后走一步 的走法
        int one = 2;//one=f(n-1)
        // 保存 最后走两步 的走法
        int two = 1; //two=f(n-2)
        //总共走法
        int sum = 0;
        for (int i = 3; i <= n; i++) {
            //f(n)=f(n-1)+f(n-2)
            sum = two + one;
            //n++; 直接下一个n 那么针对下一个n的f(n-1) f(n-2) 就变了 需要进行变换  :  当前n的 f(n-1) = 上一个n的 f(n)  ; 当前n的 f(n-2) = 上一个n的 f(n-1)
            //加了上行注释代码 应该比较容易理解流程了 尽管没有作用
            //f(n-2)=上f(n-1)
            two = one;
            //f(n-1)=上f(n)
            one = sum;
        }
        return sum;
    }

    public static void main(Strings[] args) {
        //递归做法
        System.out.println(f(4));
        System.out.println();
        //循环做法
        System.out.println(loop(4));
    }
}
