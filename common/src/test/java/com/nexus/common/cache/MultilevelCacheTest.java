package com.nexus.common.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.junit.jupiter.api.Test;
import org.springframework.cache.caffeine.CaffeineCacheManager;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

        // 创建模拟的Redis缓存管理器
        org.springframework.data.redis.cache.RedisCacheManager redisCacheManager = mock(org.springframework.data.redis.cache.RedisCacheManager.class);
        
        // 创建模拟的缓存
        org.springframework.cache.Cache mockRedisCache = mock(org.springframework.cache.Cache.class);
        when(redisCacheManager.getCache("testCache")).thenReturn(mockRedisCache);

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