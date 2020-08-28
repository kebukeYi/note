package com.java.note.Jdk.model.decorator;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author : mmy
 * @Creat Time : 2020/8/11  16:17
 * @Description
 */
@Data
public class UserCoupon {
    private int id; // 优惠券 ID
    private int userId; // 领取优惠券用户 ID
    private String sku; // 商品 SKU
    private BigDecimal coupon; // 优惠金额
}
