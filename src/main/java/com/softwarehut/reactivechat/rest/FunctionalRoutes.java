package com.softwarehut.reactivechat.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class FunctionalRoutes {
    private FunctionalHandler handler;

    public FunctionalRoutes(FunctionalHandler handler) {
        this.handler = handler;
    }

    @Bean
    RouterFunction<ServerResponse> messageRoutes() {
        return RouterFunctions.nest(
            RequestPredicates.path("/functional"),
                RouterFunctions.route(RequestPredicates.GET("/streamMessages"), handler::streamAll )
                .andRoute(RequestPredicates.GET("/rooms/{room}"), handler::streamRoom)
                .andRoute(RequestPredicates.POST("/messages"), handler::createMessage)
        );
    }

}
