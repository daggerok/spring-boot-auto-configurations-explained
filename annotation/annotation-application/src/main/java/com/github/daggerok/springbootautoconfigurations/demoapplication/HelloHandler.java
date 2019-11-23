package com.github.daggerok.springbootautoconfigurations.demoapplication;

import com.github.daggerok.springbootautoconfigurations.demolibrary.HelloService;
import io.vavr.control.Try;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;
import java.util.function.Function;

@Service
public class HelloHandler {

    private final HelloService helloService;

    public HelloHandler(HelloService helloService) {
        this.helloService = helloService;
    }

    public Mono<ServerResponse> onHello(ServerRequest request) {
        var name = request.pathVariable("name");
        var greeting = helloService.greeting(name);
        var payload = Map.of("greeting", greeting);
        return ServerResponse.ok().body(Mono.just(payload), Map.class);
    }

    public Mono<ServerResponse> onInfo(ServerRequest request) {
        var url = Try.of(request::uri)
                     .mapTry(URI::toURL)
                     .getOrElseThrow((Function<Throwable, RuntimeException>) RuntimeException::new);
        var greetingUrl = String.format("%s://%s/hello/{name}", url.getProtocol(), url.getAuthority());
        var payload = Map.of("greeting", greetingUrl);
        return ServerResponse.ok().body(Mono.just(payload), Map.class);
    }
}
