package com.java.note.Jdk.model.decorator;

import java.math.BigDecimal;

/**
 * @Author : mmy
 * @Creat Time : 2020/8/11  16:20
 * @Description 计算支付金额的抽象类
 */
public abstract class BaseCountDecorator implements IBaseCount {
    private IBaseCount count;

    public BaseCountDecorator(IBaseCount count) {
        this.count = count;
    }

    @Override
    public BigDecimal countPayMoney(OrderDetail orderDetail) {
        BigDecimal payTotalMoney = new BigDecimal(0);
        if (count != null) {
            payTotalMoney = count.countPayMoney(orderDetail);
        }
        return payTotalMoney;
    }
}
