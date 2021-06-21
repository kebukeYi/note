package com.java.note.Jdk.model.decorator;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author : mmy
 * @Creat Time : 2020/8/11  16:15
 * @Description
 */
@Data
public class Order {
    private int id; // 订单 ID
    private Strings orderNo; // 订单号
    private BigDecimal totalPayMoney; // 总支付金额
    private List<OrderDetail> list; // 详细订单列表
}


