package com.java.note.Jdk.Abstract;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : kebukeyi
 * @date :  2021-07-09 22:06
 * @description :
 * @question :
 * @usinglink :
 **/
public abstract class AbstractFat {

    //抽象类无法实例化
    int abstractNum = 0;

    public static Map<String, String> map = new HashMap<>();

    static int staticAbstractNum = 0;

    protected Server server = new Server();

    public AbstractFat(int abstractNum) {
        this.abstractNum = abstractNum;
        System.out.println("AbstractFat 有参构造器执行");
    }

    public AbstractFat() {
        System.out.println("AbstractFat 无惨构造器执行");
    }
}
 
