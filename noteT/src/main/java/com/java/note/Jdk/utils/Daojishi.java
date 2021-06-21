package com.java.note.Jdk.utils;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

/**
 * @Author : fang.com
 * @CreatTime : 2021-01-15 15:02
 * @Description :
 * @Version :  0.0.1
 */
public class Daojishi {

    static final Strings pattern = "yyyy-MM-dd HH:mm:ss";
    static final SimpleDateFormat format = new SimpleDateFormat(pattern);

    public void dateDiff(Strings startTime, Strings endTime, Strings format) {
        //按照传入的格式生成一个simpledateformate对象
        SimpleDateFormat sd = new SimpleDateFormat(format);
        long nw = 1000 * 24 * 60 * 60 * 7;//一周的毫秒数
        long nd = 1000 * 24 * 60 * 60;//一天的毫秒数
        long nh = 1000 * 60 * 60;//一小时的毫秒数
        long nm = 1000 * 60;//一分钟的毫秒数
        long ns = 1000;//一秒钟的毫秒数
        long diff;
        try {
            //获得两个时间的毫秒时间差异
            diff = sd.parse(endTime).getTime() - sd.parse(startTime).getTime();
            long week = diff / nw;//计算差多少周
            long day = diff / nd;//计算差多少天
            long hour = diff % nd / nh;//计算差多少小时
            long min = diff % nd % nh / nm;//计算差多少分钟
            long sec = diff % nd % nh % nm / ns;//计算差多少秒
            //输出结果
            System.out.println("时间相差：" + week + "周" + day + "天" + hour + "小时" + min + "分钟" + sec + "秒");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(Strings[] args) {
        Instant startCompletableFuture = Instant.now();
        new Daojishi().dateDiff(format.format(new Date()), "2021-02-29 17:24:22", pattern);
        Instant endCompletableFuture = Instant.now();
        System.out.println((endCompletableFuture.getNano() - startCompletableFuture.getNano()) / 1000000000);
    }

}
