package com.mmy.mvc.enums;

import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

/**
 * @author : kebukeyi
 * @date :  2021-09-18 21:10
 * @description :
 * @question :
 * @usinglink :
 **/
public enum CheckType {

    MOBILE("Mobile", "手机", "^1[0-9]{10}$"),
    SHOT_DATE("Date", "短日期格式", "yyyyMMdd"),
    TOOLOOG_DATE("Date", "短日期格式", "yyyyMMddHHmmssSSS"),
    DATE("Date", "常用时间格式", "yyyy-MM-dd HH:mm:ss"),
    /**
     * 请求流水号验证枚举
     */
    REQUEST_NO("RequestNo", "请求流水号", "^[0-9a-zA-Z]{32}");

    public String value;
    public String label;
    public String regular;

    private CheckType(String value, String label, String regular) {
        this.value = value;
        this.label = label;
        this.regular = regular;
    }

    public static CheckType with(String value) {
        for (CheckType type : CheckType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        return null;
    }

    /**
     * 校验方法
     * 验证传入的枚举值，是否符合规则
     *
     * @param type
     * @return true：验证成功；false：验证失败
     */
    public static boolean validate(CheckType type, Object obj) {
        if (null == obj || null == type) {
            //空不校验
            return true;
        }
        if (!(obj instanceof String)) {
            //不是String 返回false
            return false;
        }
        String str = obj.toString();
        if (StringUtils.isEmpty(str)) {
            //空字符串不校验
            return true;
        }
        //日期格式不适用正则
        if ("Date".equals(type.value)) {
            SimpleDateFormat sdf = new SimpleDateFormat(type.regular);
            try {
                sdf.parse(str);
            } catch (Exception e) {
                return false;
            }
            return true;
        }
        //使用正则校验
        return Pattern.matches(type.regular, str);
    }
}
