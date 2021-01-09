package com.mmy;

import com.mmy.webflux.config.GreetingConfig;
import com.mmy.webflux.webclient.GreetingWebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebfluxApplication {


    public static void main(String[] args) {
        SpringApplication.run(WebfluxApplication.class, args);
        GreetingWebClient gwc = new GreetingWebClient();
        System.out.println(gwc.getResult());
    }

}
