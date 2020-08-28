package com.java.note.Jdk.model.decorator;

import lombok.Data;

/**
 * @Author : mmy
 * @Creat Time : 2020/8/11  16:17
 * @Description 促销类型
 */
@Data
public class SupportPromotions implements Cloneable {

    private int id;// 该商品促销的 ID
    private PromotionType promotionType;// 促销类型 1\优惠券 2\红包
    private int priority; // 优先级
    private UserCoupon userCoupon; // 用户领取该商品的优惠券
    private UserRedPacket userRedPacket; // 用户领取该商品的红包

    // 重写 clone 方法
    @Override
    public SupportPromotions clone() {
        SupportPromotions supportPromotions = null;
        try {
            supportPromotions = (SupportPromotions) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return supportPromotions;
    }

}
