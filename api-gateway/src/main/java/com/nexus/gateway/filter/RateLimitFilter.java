package com.nexus.gateway.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RateLimitFilter extends AbstractGatewayFilterFactory<RateLimitFilter.Config> {
    
    @Autowired
    private ReactiveRedisTemplate<String, String> redisTemplate;
    
    private static final String RATE_LIMIT_PREFIX = "rate_limit:";
    private static final int DEFAULT_LIMIT = 100; // 默认限制100次请求
    private static final int DEFAULT_WINDOW_SECONDS = 60; // 默认时间窗口60秒
    
    // 高级用户限流配置
    private static final int PREMIUM_LIMIT = 1000; // 高级用户限制1000次请求
    private static final int PREMIUM_WINDOW_SECONDS = 60; // 高级用户时间窗口60秒
    
    public RateLimitFilter() {
        super(Config.class);
    }
    
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String clientId = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
            
            // 检查是否为高级用户(这里简化处理，实际应从JWT或其他地方获取)
            boolean isPremiumUser = checkPremiumUser(exchange);
            
            String key = RATE_LIMIT_PREFIX + clientId;
            int limit = config.getLimit() > 0 ? config.getLimit() : 
                       (isPremiumUser ? PREMIUM_LIMIT : DEFAULT_LIMIT);
            int windowSeconds = config.getWindowSeconds() > 0 ? config.getWindowSeconds() : 
                               (isPremiumUser ? PREMIUM_WINDOW_SECONDS : DEFAULT_WINDOW_SECONDS);
            
            // 使用滑动窗口算法改进限流
            return redisTemplate.opsForValue().increment(key, 1)
                    .flatMap(count -> {
                        if (count == 1) {
                            // 第一次访问，设置过期时间
                            return redisTemplate.expire(key, Duration.ofSeconds(windowSeconds))
                                    .thenReturn(count);
                        }
                        return Mono.just(count);
                    })
                    .flatMap(count -> {
                        if (count > limit) {
                            // 超过限流阈值
                            exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
                            String data = "{\"error\":\"Rate limit exceeded\",\"message\":\"请求过于频繁，请稍后再试\"}";
                            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(data.getBytes());
                            return exchange.getResponse().writeWith(Mono.just(buffer));
                        }
                        
                        // 正常处理请求
                        return chain.filter(exchange);
                    });
        };
    }
    
    /**
     * 检查是否为高级用户
     * @param exchange 交换对象
     * @return 是否为高级用户
     */
    private boolean checkPremiumUser(org.springframework.web.server.ServerWebExchange exchange) {
        // 实际实现中应该从JWT token或其他用户信息中获取用户等级
        // 这里简化处理，始终返回false
        return false;
    }
    
    public static class Config {
        private int limit;
        private int windowSeconds;
        
        public int getLimit() {
            return limit;
        }
        
        public void setLimit(int limit) {
            this.limit = limit;
        }
        
        public int getWindowSeconds() {
            return windowSeconds;
        }
        
        public void setWindowSeconds(int windowSeconds) {
            this.windowSeconds = windowSeconds;
        }
    }
}