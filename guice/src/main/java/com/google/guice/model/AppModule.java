package com.google.guice.model;

import com.google.guice.service.Application;
import com.google.guice.service.LogService;
import com.google.guice.service.UserService;
import com.google.guice.service.lmpl.App;
import com.google.guice.service.lmpl.LogServiceImpl;
import com.google.guice.service.lmpl.UserServicelmpl;
import com.google.inject.AbstractModule;

/**
 * @Author : fang.com
 * @CreatTime : 2021-02-02 09:44
 * @Description : 当Guice遇到接口或父类需要注入具体实现的时候，就会使用这里配置的实现类或子类来注入
 * @Version :  0.0.1
 */
public class AppModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(LogService.class).to(LogServiceImpl.class);
        bind(UserService.class).to(UserServicelmpl.class);
        bind(Application.class).to(App.class);
    }
}
