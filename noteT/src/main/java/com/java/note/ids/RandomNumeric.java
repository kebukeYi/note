package com.java.note.ids;


import org.apache.commons.lang3.RandomStringUtils;


/**
 * @description: 工具类生成 org.apache.commons.lang3.RandomStringUtils
 * @author: 小傅哥，微信：fustack
 * @date: 2021/9/20
 * @github: https://github.com/fuzhengwei
 * @Copyright: 公众号：bugstack虫洞栈 | 博客：https://bugstack.cn - 沉淀、分享、成长，让自己和他人都能有所收获！
 */

public class RandomNumeric {


    public long nextId() {
        return Long.parseLong(RandomStringUtils.randomNumeric(11));
    }

}
