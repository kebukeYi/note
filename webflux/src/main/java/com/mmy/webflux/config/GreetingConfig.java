package com.mmy.webflux.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GreetingConfig {

    @Value("${server.port}")
    public Integer port;

}
