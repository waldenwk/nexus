package com.nexus.common.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MultilevelCacheTest {

    @Test
    public void testMultilevelCache() {
        // 创建Caffeine缓存管理器
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
        caffeineCacheManager.setCaffeine(Caffeine.newBuilder()
                .initialCapacity(100)
                .maximumSize(1000)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .expireAfterAccess(5, TimeUnit.MINUTES)
                .recordStats());

        // 创建Redis连接工厂
        RedisConnectionFactory redisConnectionFactory = new LettuceConnectionFactory();
        
        // 创建RedisTemplate
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.afterPropertiesSet();
        
        // 创建Redis缓存管理器
        RedisCacheManager redisCacheManager = RedisCacheManager.builder(redisConnectionFactory).build();

        // 创建多级缓存管理器
        MultilevelCacheManager multilevelCacheManager = new MultilevelCacheManager(caffeineCacheManager, redisCacheManager);
        
        // 获取多级缓存
        MultilevelCacheManager.MultilevelCache cache = multilevelCacheManager.getMultilevelCache("testCache");
        
        // 测试放入和获取数据
        String key = "testKey";
        String value = "testValue";
        
        cache.put(key, value);
        
        String cachedValue = cache.get(key, String.class);
        assertEquals(value, cachedValue);
        
        // 测试删除数据
        cache.evict(key);
        String deletedValue = cache.get(key, String.class);
        assertNull(deletedValue);
    }
}