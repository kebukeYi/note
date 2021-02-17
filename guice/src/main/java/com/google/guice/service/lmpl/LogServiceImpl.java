package com.google.guice.service.lmpl;

import com.google.guice.service.LogService;

/**
 * @Author : fang.com
 * @CreatTime : 2021-02-02 09:36
 * @Description :
 * @Version :  0.0.1
 */
public class LogServiceImpl implements LogService {
    @Override
    public void log(String msg) {
        System.out.println("------LOG:" + msg);
    }
}
