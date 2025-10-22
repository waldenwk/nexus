package com.nexus.feed.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Feed服务类，负责处理用户信息流相关业务逻辑
 * 使用Redis的有序集合(ZSet)存储用户feed，实现高性能的信息流读写
 */
@Service
public class FeedService {
    
    /** Redis操作模板 */
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    
    /** Feed键前缀 */
    private static final String FEED_KEY_PREFIX = "user_feed:";
    
    /**
     * 将内容推送给用户的feed
     * 使用Redis有序集合存储，以时间戳作为排序分数
     * @param userId 用户ID
     * @param contentId 内容ID
     * @param score 排序分数（通常是时间戳）
     */
    public void pushToFeed(Long userId, Long contentId, double score) {
        String feedKey = FEED_KEY_PREFIX + userId;
        redisTemplate.opsForZSet().add(feedKey, contentId.toString(), score);
    }
    
    /**
     * 获取用户的feed
     * 按时间倒序排列（最新的在前）
     * @param userId 用户ID
     * @param start 起始位置
     * @param end 结束位置
     * @return 内容ID集合
     */
    public Set<String> getUserFeed(Long userId, int start, int end) {
        String feedKey = FEED_KEY_PREFIX + userId;
        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
        return zSetOps.reverseRange(feedKey, start, end);
    }
    
    /**
     * 从feed中删除指定内容
     * 当内容被删除或用户关系变更时调用
     * @param userId 用户ID
     * @param contentId 内容ID
     */
    public void removeFromFeed(Long userId, Long contentId) {
        String feedKey = FEED_KEY_PREFIX + userId;
        redisTemplate.opsForZSet().remove(feedKey, contentId.toString());
    }
}