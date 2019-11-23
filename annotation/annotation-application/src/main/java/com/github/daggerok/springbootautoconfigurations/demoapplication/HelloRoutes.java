package com.github.daggerok.springbootautoconfigurations.demoapplication;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;

@Configuration
public class HelloRoutes {

    @Bean
    RouterFunction routes(HelloHandler handler) {
        return RouterFunctions.route()
                              .GET("/hello/{name}", handler::onHello)
                              .build()
                              .andRoute(path("/**"), handler::onInfo);
    }
}
