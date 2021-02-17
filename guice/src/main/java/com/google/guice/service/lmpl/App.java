package com.google.guice.service.lmpl;

import com.google.guice.service.Application;
import com.google.guice.service.LogService;
import com.google.guice.service.UserService;
import com.google.inject.Inject;

/**
 * @Author : fang.com
 * @CreatTime : 2021-02-02 09:39
 * @Description :
 * @Version :  0.0.1
 */
public class App implements Application {

    private UserService userService;
    private LogService logService;

    @Inject
    public App(UserService userService, LogService logService) {
        this.userService = userService;
        this.logService = logService;
    }

    @Override
    public void work() {
        userService.process();
        logService.log("程序正常运行");
    }
}
