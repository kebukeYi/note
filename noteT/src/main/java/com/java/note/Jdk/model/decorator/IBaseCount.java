package com.java.note.Jdk.model.decorator;

import java.math.BigDecimal;

/**
 * @Author : mmy
 * @Creat Time : 2020/8/11  16:18
 * @Description 计算支付金额接口类
 */
public interface IBaseCount {

    BigDecimal countPayMoney(OrderDetail orderDetail);

}
