package com.java.note.Jdk.model.builders;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/28  8:45
 * @Description 抽象建造者方法
 */
public abstract class Builder {

    abstract Builder buildA();

    abstract Builder buildB();

    abstract Builder buildC();

    abstract Builder buildD();

    //完工 得到产品
    abstract Product getProduct();


}
