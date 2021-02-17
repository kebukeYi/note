package com.google.guice;

import com.google.guice.model.AppModule;
import com.google.guice.service.Application;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @Author : fang.com
 * @CreatTime : 2021-02-02 09:47
 * @Description :
 * @Version :  0.0.1
 */
public class AppTest {
    private static Injector injector;

    @BeforeClass
    public static void init() {
        injector = Guice.createInjector(new AppModule());
    }

    @Test
    public void testMyApp() {
        Application myApp = injector.getInstance(Application.class);
        myApp.work();
    }
}
