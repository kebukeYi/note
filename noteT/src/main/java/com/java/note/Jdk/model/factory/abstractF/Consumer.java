package com.java.note.Jdk.model.factory.abstractF;

import java.util.Calendar;
import java.util.Date;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/28  7:28
 * @Description
 */
public class Consumer {


    public static void main(String[] args) {

        MiFactory miFactory = new MiFactory();
        IRouterProduct iRouterProduct = miFactory.getIRouterProduct();
        PhoneProduct product = miFactory.getPhoneProduct();
        iRouterProduct.start();
        product.shutdown();

        HuaWeiFactory huaWeiFactory = new HuaWeiFactory();
        IRouterProduct huaWeiFactoryIRouterProduct = huaWeiFactory.getIRouterProduct();
        huaWeiFactoryIRouterProduct.start();

        Calendar.getInstance();


    }
}
