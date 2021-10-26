package com.java.interview.thread.volatiles;

/**
 * @author : kebukeYi
 * @date :  2021-10-25 21:12
 * @description:
 * @question:
 * @link:
 **/
public class VolatileVar {

    //使用 volatile 保障内存可见性
    volatile int var = 0;

    public void setVar(int var) {
        System.out.println("setVar = " + var);
        this.var = var;
    }

    public static void main(String[] args) {
        VolatileVar var = new VolatileVar();
        var.setVar(100);
    }
}
 
