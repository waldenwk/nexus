package com.nexus.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
    
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-service", r -> r.path("/api/users/**")
                        .uri("lb://user-service"))
                .route("content-service", r -> r.path("/api/content/**")
                        .uri("lb://content-service"))
                .route("message-service", r -> r.path("/api/messages/**")
                        .uri("lb://message-service"))
                .route("feed-service", r -> r.path("/api/feed/**")
                        .uri("lb://feed-service"))
                .route("group-service", r -> r.path("/api/groups/**")
                        .uri("lb://group-service"))
                .route("event-service", r -> r.path("/api/events/**")
                        .uri("lb://event-service"))
                .route("search-service", r -> r.path("/api/search/**")
                        .uri("lb://search-service"))
                .route("file-service", r -> r.path("/api/files/**")
                        .uri("lb://file-service"))
                .route("auth-service", r -> r.path("/api/auth/**")
                        .uri("lb://auth-service"))
                .build();
    }
}