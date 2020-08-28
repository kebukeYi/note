package com.java.note.Jdk.model.factory.abstractF;

/**
 * @Author : mmy
 * @Creat Time : 2020/6/28  7:26
 * @Description
 */
public class HuaWeiFactory implements ProductFactory {


    @Override
    public PhoneProduct getPhoneProduct() {
        return new HuaWeiPhone();
    }

    @Override
    public IRouterProduct getIRouterProduct() {
        return new HuaWeiRouter();
    }
}
