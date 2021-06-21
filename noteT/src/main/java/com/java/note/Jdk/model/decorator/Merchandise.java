package com.java.note.Jdk.model.decorator;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @Author : mmy
 * @Creat Time : 2020/8/11  16:16
 * @Description
 */
@Data
public class Merchandise {
    private Strings sku;// 商品 SKU
    private Strings name; // 商品名称
    private BigDecimal price; // 商品单价
    private Map<PromotionType, SupportPromotions> supportPromotions; // 支持促销类型
}


