package com.java.interview.thread.consumer_provider;

/**
 * @author : kebukeYi
 * @date :  2021-10-21 23:08
 * @description:
 * @question:
 * @link:
 **/
public class Goods {


    public static IGoods produceOne() {
        return new IGoods("JOJO", 222);
    }


}
 
