package com.adriana.reactivoHastaElFinal.controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.reactive.ClientWebRequestBuilders;
import org.springframework.web.client.reactive.ResponseExtractors;
import org.springframework.web.client.reactive.WebClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;


@SpringBootApplication
@RestController
public class ControllerResult {

    @Value("${app.url:http://example.com}")
    private String url = "http://example.com";

    private static Logger log = LoggerFactory.getLogger(ReactiveApplication.class);
    private RestTemplate restTemplate = new RestTemplate();
    private WebClient client = new WebClient(new ReactorClientHttpConnector());
    private Scheduler scheduler = Schedulers.elastic();

    private WebClient client = new WebClient(new ());

    @RequestMapping("/netty")
    public Mono<Result> netty() {
        log.info("Handling /netty");
        return Flux.range(1, 10)
                .log()
                .flatMap(this::fetch)
                .collect(Result::new, Result::add)
                .doOnSuccess(Result::stop);

    }

    private HttpStatus block(int value) {
        return this.restTemplate.getForEntity(url, String.class, value).getStatusCode();
    }

    private Mono<HttpStatus> fetch(int value) {
        return this.client.perform(ClientWebRequestBuilders.get(url)).extract(ResponseExtractors.response(String.class))
                .map(response -> response.getStatusCode());
    }

    public static void main(String[] args) {
        // System.setProperty("reactor.io.epoll", "false");
        SpringApplication.run(ReactiveApplication.class, args);
    }

}

}


