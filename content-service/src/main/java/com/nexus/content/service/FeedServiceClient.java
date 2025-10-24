package com.nexus.content.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Feed服务客户端接口
 * 通过Feign实现服务间调用，获取用户信息流内容
 */
@FeignClient(name = "feed-service")
public interface FeedServiceClient {
    
    /**
     * 获取用户feed
     * @param userId 用户ID
     * @param start 起始位置
     * @param end 结束位置
     * @return 内容ID列表
     */
    @GetMapping("/api/feed/{userId}")
    List<Long> getUserFeed(@PathVariable("userId") Long userId,
                          @RequestParam("start") int start,
                          @RequestParam("end") int end);
    
    /**
     * 推送内容到用户feed
     * @param userId 用户ID
     * @param contentId 内容ID
     * @param score 分数
     */
    @PostMapping("/api/feed/push")
    void pushToFeed(@RequestParam("userId") Long userId,
                    @RequestParam("contentId") Long contentId,
                    @RequestParam("score") double score);
}