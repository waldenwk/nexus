package com.nexus.common.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;

/**
 * 多级缓存管理器
 * 实现两级缓存：一级本地缓存(Caffeine)，二级分布式缓存(Redis)
 */
@Component
public class MultilevelCacheManager {

    private static final Logger logger = LoggerFactory.getLogger(MultilevelCacheManager.class);

    // 本地缓存管理器
    private final CaffeineCacheManager caffeineCacheManager;

    // Redis缓存管理器
    private final RedisCacheManager redisCacheManager;

    @Autowired
    public MultilevelCacheManager(CaffeineCacheManager caffeineCacheManager,
                                  RedisCacheManager redisCacheManager) {
        this.caffeineCacheManager = caffeineCacheManager;
        this.redisCacheManager = redisCacheManager;
    }

    /**
     * 获取多级缓存对象
     *
     * @param cacheName 缓存名称
     * @return 多级缓存实现
     */
    public MultilevelCache getMultilevelCache(String cacheName) {
        return new MultilevelCache(cacheName, caffeineCacheManager, redisCacheManager);
    }

    /**
     * 多级缓存实现类
     */
    public static class MultilevelCache implements Cache {

        private final String name;
        private final Cache localCache;
        private final Cache redisCache;

        public MultilevelCache(String name, CaffeineCacheManager caffeineCacheManager,
                               RedisCacheManager redisCacheManager) {
            this.name = name;
            this.localCache = caffeineCacheManager.getCache(name);
            this.redisCache = redisCacheManager.getCache(name);
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Object getNativeCache() {
            return this;
        }

        @Override
        public ValueWrapper get(Object key) {
            // 先从本地缓存获取
            ValueWrapper valueWrapper = localCache.get(key);
            if (valueWrapper != null) {
                logger.debug("从本地缓存获取数据，key: {}", key);
                return valueWrapper;
            }

            // 本地缓存未命中，从Redis缓存获取
            valueWrapper = redisCache.get(key);
            if (valueWrapper != null) {
                logger.debug("从Redis缓存获取数据，key: {}", key);
                // 将数据放入本地缓存
                localCache.put(key, valueWrapper.get());
                return valueWrapper;
            }

            logger.debug("缓存未命中，key: {}", key);
            return null;
        }

        @Override
        public <T> T get(Object key, Class<T> type) {
            // 先从本地缓存获取
            T value = localCache.get(key, type);
            if (value != null) {
                logger.debug("从本地缓存获取数据，key: {}", key);
                return value;
            }

            // 本地缓存未命中，从Redis缓存获取
            value = redisCache.get(key, type);
            if (value != null) {
                logger.debug("从Redis缓存获取数据，key: {}", key);
                // 将数据放入本地缓存
                localCache.put(key, value);
                return value;
            }

            logger.debug("缓存未命中，key: {}", key);
            return null;
        }

        @Override
        public <T> T get(Object key, Callable<T> valueLoader) {
            // 先从本地缓存获取
            ValueWrapper valueWrapper = localCache.get(key);
            if (valueWrapper != null) {
                logger.debug("从本地缓存获取数据，key: {}", key);
                @SuppressWarnings("unchecked")
                T value = (T) valueWrapper.get();
                return value;
            }

            // 本地缓存未命中，从Redis缓存获取
            valueWrapper = redisCache.get(key);
            if (valueWrapper != null) {
                logger.debug("从Redis缓存获取数据，key: {}", key);
                @SuppressWarnings("unchecked")
                T value = (T) valueWrapper.get();
                // 将数据放入本地缓存
                localCache.put(key, value);
                return value;
            }

            // 都未命中，加载数据
            try {
                T value = valueLoader.call();
                if (value != null) {
                    // 将数据放入两级缓存
                    localCache.put(key, value);
                    redisCache.put(key, value);
                }
                return value;
            } catch (Exception e) {
                throw new CacheValueRetrievalException("加载缓存值失败", e);
            }
        }

        @Override
        public void put(Object key, Object value) {
            // 同时放入本地缓存和Redis缓存
            localCache.put(key, value);
            redisCache.put(key, value);
            logger.debug("数据已放入多级缓存，key: {}", key);
        }

        @Override
        public void evict(Object key) {
            // 同时删除本地缓存和Redis缓存
            localCache.evict(key);
            redisCache.evict(key);
            logger.debug("从多级缓存中删除数据，key: {}", key);
        }

        @Override
        public void clear() {
            // 清空本地缓存和Redis缓存
            localCache.clear();
            redisCache.clear();
            logger.debug("清空多级缓存");
        }
    }
}