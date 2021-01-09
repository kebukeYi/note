package com.mmy.webflux.webclient;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class GreetingWebClient {

    private WebClient client = WebClient.create("http://localhost:8080");
    private Mono<ClientResponse> result = client.get().uri("/hello").accept(MediaType.TEXT_PLAIN).exchange();

    public String getResult() {
        //主动阻塞等待结果
        return ">> result = " + result.flatMap(res -> res.bodyToMono(String.class)).block();
//        return ">> result = " + result.flatMap(res -> res.bodyToMono(String.class));
//        return ">> result = " + result.flatMap(res -> res.bodyToMono(String.class)).flux();
    }

}
