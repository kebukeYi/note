package com.java.note.Jdk.model.decorator;

import java.math.BigDecimal;

/**
 * @Author : mmy
 * @Creat Time : 2020/8/11  16:19
 * @Description
 */
public class BaseCount implements IBaseCount {

    public BigDecimal countPayMoney(OrderDetail orderDetail) {
        orderDetail.setPayMoney(orderDetail.getMerchandise().getPrice());
        System.out.println(" 商品原单价金额为：" + orderDetail.getPayMoney());

        return orderDetail.getPayMoney();
    }
}
