package com.nexus.common.cache;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 缓存配置入口
 * 启用缓存功能并导入相关配置
 */
@Configuration
@EnableCaching
@Import({CacheConfig.class, MultilevelCacheAspect.class})
public class CacheConfiguration {
}