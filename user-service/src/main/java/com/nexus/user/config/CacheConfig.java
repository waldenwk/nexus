package com.nexus.user.config;

import com.nexus.common.cache.CacheConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 用户服务缓存配置
 * 导入通用缓存配置
 */
@Configuration
@Import(CacheConfiguration.class)
public class CacheConfig {
}