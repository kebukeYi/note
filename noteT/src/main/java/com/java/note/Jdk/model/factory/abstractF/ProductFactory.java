package com.java.note.Jdk.model.factory.abstractF;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/28  7:25
 * @Description 抽象工厂就是一个超级工厂创建其他工厂，该超级工厂又成为其他工厂的工厂；
 */
public interface ProductFactory {

    PhoneProduct getPhoneProduct();

    IRouterProduct getIRouterProduct();


}
