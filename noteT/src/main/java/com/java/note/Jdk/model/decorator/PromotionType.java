package com.java.note.Jdk.model.decorator;

/**
 * @Author : mmy
 * @Creat Time : 2020/8/11  16:22
 * @Description
 */
public enum PromotionType {

    COUPON("优惠卷", 1), REDPACKED("红包", 2);

    // 成员变量
    private String name;
    private int index;

    // 构造方法
    PromotionType(String name, int index) {
        this.name = name;
        this.index = index;
    }

    // get set 方法
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getIndex() {
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }



}
