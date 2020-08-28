package com.java.note.Jdk.model.factory.abstractF;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/28  7:24
 * @Description
 */
public class MiFactory implements ProductFactory {


    @Override
    public PhoneProduct getPhoneProduct() {
        return new MiPhone();
    }

    @Override
    public IRouterProduct getIRouterProduct() {
        return new MiRouter();
    }
}
