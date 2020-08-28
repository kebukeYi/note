package com.java.note.Jdk.model.decorator;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author : mmy
 * @Creat Time : 2020/8/11  16:18
 * @Description
 */
@Data
public class UserRedPacket {
    private int id; // 红包 ID
    private int userId; // 领取用户 ID
    private String sku; // 商品 SKU
    private BigDecimal redPacket; // 领取红包金额
}
