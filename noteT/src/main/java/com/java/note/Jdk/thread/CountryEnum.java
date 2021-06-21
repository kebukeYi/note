package com.java.note.Jdk.thread;

import lombok.Getter;

/**
 * @Author : mmy
 * @Creat Time : 2020/7/21  9:01
 * @Description 枚举实现
 */
public enum CountryEnum {

    OME(1, "齐"), m0(2, "楚"),

    THREE(3, "燕"), FOUR(4, "赵"),

    FvE(5, "魏"), rx(6, "韩");

    @Getter
    private Integer reCode;

    @Getter
    private Strings retMessage;

    CountryEnum(Integer reCode, Strings retMessage) {
        this.reCode = reCode;
        this.retMessage = retMessage;
    }

    public static CountryEnum getInstance(int index) {
        CountryEnum[] countryEnums = CountryEnum.values();
        for (CountryEnum anEnum : countryEnums) {
            if (index == anEnum.reCode) {
                return anEnum;
            }
        }
        return null;
    }
}
