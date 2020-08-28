package com.java.note.Jdk.model.decorator;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author : mmy
 * @Creat Time : 2020/8/11  16:15
 * @Description
 */
@Data
public class OrderDetail {
    private int id; // 详细订单 ID
    private int orderId;// 主订单 ID
    private Merchandise merchandise; // 商品详情
    private BigDecimal payMoney; // 支付单价

}
