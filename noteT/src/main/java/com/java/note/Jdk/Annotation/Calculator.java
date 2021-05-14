package com.java.note.Jdk.Annotation;

/**
 * @author : kebukeyi
 * @date :  2021-04-13 17:47
 * @description :
 **/
public class Calculator {

    /** 加法 */
    @Check
    public int add(int a, int b) {
        return a + b;
    }

    /** 减法 */
    @Check
    public int sub(int a, int b) {
        return a - b;
    }

    /** 乘法 */
    @Check
    public int mul(int a, int b) {
        return a * b;
    }

    /** 除法 */
    @Check
    public int div(int a, int b) {
        return a / b;
    }

    public void method() {
        System.out.println("永无bug！！！");
    }

}
 
