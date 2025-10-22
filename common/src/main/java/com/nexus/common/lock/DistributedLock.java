package com.nexus.common.lock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁实现
 * 基于Redis实现分布式锁，用于分布式环境下的资源同步
 */
@Component
public class DistributedLock {
    
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    
    /**
     * 获取分布式锁
     * @param lockKey 锁key
     * @param lockValue 锁value
     * @param expireTime 过期时间（秒）
     * @return 是否获取成功
     */
    public boolean lock(String lockKey, String lockValue, long expireTime) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        Boolean result = ops.setIfAbsent(lockKey, lockValue, expireTime, TimeUnit.SECONDS);
        return result != null && result;
    }
    
    /**
     * 释放分布式锁
     * @param lockKey 锁key
     * @param lockValue 锁value
     * @return 是否释放成功
     */
    public boolean unlock(String lockKey, String lockValue) {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                       "return redis.call('del', KEYS[1]) " +
                       "else return 0 end";
        
        // 使用Lua脚本确保原子性
        Object result = redisTemplate.execute(
            (connection) -> connection.eval(
                script.getBytes(),
                org.springframework.data.redis.connection.ReturnType.INTEGER,
                1,
                lockKey.getBytes(),
                lockValue.getBytes()
            )
        );
        
        return result != null && "1".equals(result.toString());
    }
    
    /**
     * 尝试获取分布式锁（带重试机制）
     * @param lockKey 锁key
     * @param lockValue 锁value
     * @param expireTime 过期时间（秒）
     * @param retryTimes 重试次数
     * @param sleepTime 重试间隔（毫秒）
     * @return 是否获取成功
     */
    public boolean tryLock(String lockKey, String lockValue, long expireTime, int retryTimes, long sleepTime) {
        boolean locked = false;
        int tryCount = 0;
        while (!locked && tryCount < retryTimes) {
            locked = lock(lockKey, lockValue, expireTime);
            if (!locked) {
                tryCount++;
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
        return locked;
    }
}