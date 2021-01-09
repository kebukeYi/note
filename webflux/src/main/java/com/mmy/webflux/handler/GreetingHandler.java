package com.mmy.webflux.handler;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class GreetingHandler {

    public Mono<ServerResponse> getHello(ServerRequest request) {
        try {
            System.out.println("开始睡眠！" + System.currentTimeMillis() / 1000);
            Thread.sleep(3000);
            System.out.println("结束睡眠！" + System.currentTimeMillis() / 1000);
            System.out.println("request：" + request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN).body(BodyInserters.fromValue("模拟远程调用！"));
    }


}
