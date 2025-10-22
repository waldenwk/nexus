package com.nexus.gateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {
    
    @Value("${jwt.secret:nexus_secret_key}")
    private String secret;
    
    private static final List<String> EXCLUDE_URL = List.of(
            "/api/users/login",
            "/api/users/register",
            "/api/auth/**"
    );
    
    public JwtAuthenticationFilter() {
        super(Config.class);
    }
    
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            
            // 检查是否需要跳过认证
            if (isExcludeUrl(request.getURI().getPath())) {
                return chain.filter(exchange);
            }
            
            // 检查Authorization头
            String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                
                try {
                    // 验证JWT token
                    Claims claims = Jwts.parser()
                            .setSigningKey(secret)
                            .parseClaimsJws(token)
                            .getBody();
                    
                    // 将用户信息添加到请求头中
                    ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                            .header("X-User-ID", claims.getSubject())
                            .build();
                    
                    ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();
                    
                    return chain.filter(mutatedExchange);
                } catch (Exception e) {
                    // Token无效
                    ServerHttpResponse response = exchange.getResponse();
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    return response.setComplete();
                }
            }
            
            // 没有提供token
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        };
    }
    
    private boolean isExcludeUrl(String url) {
        return EXCLUDE_URL.stream().anyMatch(url::startsWith);
    }
    
    public static class Config {
        // 过滤器配置
    }
}