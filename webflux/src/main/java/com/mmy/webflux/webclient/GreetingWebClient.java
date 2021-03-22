package com.mmy.webflux.webclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.WebServer;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

public class GreetingWebClient {

    @Value("${server.port}")
    public Integer port;

    private WebClient client = WebClient.create("http://localhost:" + Optional.ofNullable(port).orElse(8082));

    private Mono<ClientResponse> result = client.get().uri("/hello").accept(MediaType.TEXT_PLAIN).exchange();

    public String getResult() {
        //用的是哪个线程 阻塞获取值的？
        //主动阻塞等待结果
        return ">> result = " + result.flatMap(res -> res.bodyToMono(String.class)).block();
//        return ">> result = " + result.flatMap(res -> res.bodyToMono(String.class));
//        return ">> result = " + result.flatMap(res -> res.bodyToMono(String.class)).flux();
    }

}
