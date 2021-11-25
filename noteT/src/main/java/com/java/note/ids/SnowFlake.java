package com.java.note.ids;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.IdUtil;


import javax.annotation.PostConstruct;

/**
 * @description: hutool 工具包下的雪花算法，15位雪花算法推荐：https://github.com/yitter/idgenerator/blob/master/Java/source/src/main/java/com/github/yitter/core/SnowWorkerM1.java
 * @author: 小傅哥，微信：fustack
 * @date: 2021/9/20
 * @github: https://github.com/fuzhengwei
 * @Copyright: 公众号：bugstack虫洞栈 | 博客：https://bugstack.cn - 沉淀、分享、成长，让自己和他人都能有所收获！
 */

public class SnowFlake {

    private Snowflake snowflake;

    @PostConstruct
    public void init() {
        // 0 ~ 31 位，可以采用配置的方式使用
        long workerId;
        try {
            //192.168.1.13
            final String localhostStr = NetUtil.getLocalhostStr();
            //3232235789
            workerId = NetUtil.ipv4ToLong(localhostStr);
        } catch (Exception e) {
            workerId = NetUtil.getLocalhostStr().hashCode();
        }
        //8
        workerId = workerId >> 16 & 31;

        long dataCenterId = 1L;
        snowflake = IdUtil.createSnowflake(workerId, dataCenterId);
    }

    public synchronized long nextId() {
        return snowflake.nextId();
    }

    public static void main(String[] args) {
        SnowFlake snowFlake = new SnowFlake();
        snowFlake.init();
    }

}
