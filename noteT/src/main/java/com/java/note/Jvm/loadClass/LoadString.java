package com.java.note.Jvm.loadClass;

//import java.lang.String;

/**
 * @author : kebukeyi
 * @date :  2021-06-21 13:28
 * @description :
 * @question :
 * @usinglink :
 **/
public class LoadString {

    public static void main(String[] args) {
        loadString();
    }

    public static void loadString() {
        //自定义 String 类
        String String = new String();
        System.out.println(String);
    }
}
 
