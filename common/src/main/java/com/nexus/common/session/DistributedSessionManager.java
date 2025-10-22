package com.nexus.common.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 分布式Session管理器
 * 使用Redis存储Session信息，实现Session共享
 */
@Component
public class DistributedSessionManager {
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    // Session过期时间（分钟）
    private static final long SESSION_TIMEOUT = 30;
    
    /**
     * 创建Session
     * @param sessionId Session ID
     * @param userId 用户ID
     * @param userInfo 用户信息
     */
    public void createSession(String sessionId, Long userId, Object userInfo) {
        String sessionKey = "session:" + sessionId;
        redisTemplate.opsForValue().set(sessionKey, userInfo, SESSION_TIMEOUT, TimeUnit.MINUTES);
        // 同时存储用户ID与Session ID的映射关系，用于根据用户ID查找Session
        redisTemplate.opsForValue().set("user_session:" + userId, sessionId, SESSION_TIMEOUT, TimeUnit.MINUTES);
    }
    
    /**
     * 获取Session信息
     * @param sessionId Session ID
     * @return 用户信息
     */
    public Object getSession(String sessionId) {
        String sessionKey = "session:" + sessionId;
        return redisTemplate.opsForValue().get(sessionKey);
    }
    
    /**
     * 更新Session过期时间
     * @param sessionId Session ID
     */
    public void refreshSession(String sessionId) {
        String sessionKey = "session:" + sessionId;
        redisTemplate.expire(sessionKey, SESSION_TIMEOUT, TimeUnit.MINUTES);
    }
    
    /**
     * 删除Session
     * @param sessionId Session ID
     */
    public void removeSession(String sessionId) {
        String sessionKey = "session:" + sessionId;
        redisTemplate.delete(sessionKey);
    }
    
    /**
     * 根据用户ID获取Session ID
     * @param userId 用户ID
     * @return Session ID
     */
    public String getSessionIdByUserId(Long userId) {
        return (String) redisTemplate.opsForValue().get("user_session:" + userId);
    }
    
    /**
     * 删除用户Session映射
     * @param userId 用户ID
     */
    public void removeUserSessionMapping(Long userId) {
        redisTemplate.delete("user_session:" + userId);
    }
}