package cn.gof.flyweight.java;

/**
 * @author : kebukeyi
 * @date :  2021-07-19 19:38
 * @description:
 * @question:
 * @link:
 **/
public class StringDemo {
    public static void main(String[] args) {

        String s1 = "小争哥";
        String s2 = "小争哥";
        String s3 = new String("小争哥").intern();
        String s4 = new String("小争哥");

        System.out.println(s1 == s2);
        System.out.println(s1 == s3);
        System.out.println(s1 == s4);
    }
}
 
