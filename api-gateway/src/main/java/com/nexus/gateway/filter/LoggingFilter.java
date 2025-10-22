package com.nexus.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Date;

@Component
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {
    
    public LoggingFilter() {
        super(Config.class);
    }
    
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            
            // 记录请求开始时间
            long startTime = System.currentTimeMillis();
            String method = request.getMethodValue();
            String url = request.getURI().toString();
            String clientIp = request.getRemoteAddress() != null ? 
                    request.getRemoteAddress().getAddress().getHostAddress() : "unknown";
            
            System.out.println(String.format("[%s] Request: %s %s from %s", 
                    new Date(startTime), method, url, clientIp));
            
            return chain.filter(exchange).then(
                    Mono.fromRunnable(() -> {
                        ServerHttpResponse response = exchange.getResponse();
                        long endTime = System.currentTimeMillis();
                        long duration = endTime - startTime;
                        
                        System.out.println(String.format("[%s] Response: %s %s - Status: %d - Duration: %d ms", 
                                new Date(endTime), method, url, response.getStatusCode().value(), duration));
                    })
            );
        };
    }
    
    public static class Config {
        // 过滤器配置
    }
}