package com.nexus.common.cache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 多级缓存注解
 * 用于方法级别缓存，支持两级缓存：本地缓存 + Redis缓存
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MultilevelCacheable {
    
    /**
     * 缓存名称
     * @return 缓存名称
     */
    String cacheName();
    
    /**
     * 缓存键
     * 支持SpEL表达式
     * @return 缓存键
     */
    String key();
    
    /**
     * 缓存过期时间（秒）
     * @return 过期时间
     */
    long expireTime() default 600;
}