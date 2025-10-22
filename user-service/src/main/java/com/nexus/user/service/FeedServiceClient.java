package com.nexus.user.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "feed-service")
public interface FeedServiceClient {
    
    /**
     * 当添加好友时更新feed
     * @param userId 用户ID
     * @param friendId 好友ID
     */
    @PostMapping("/api/feed/update/friend-added")
    void updateFeedOnFriendAdded(@RequestParam("userId") Long userId, 
                                @RequestParam("friendId") Long friendId);
    
    /**
     * 当移除好友时更新feed
     * @param userId 用户ID
     * @param friendId 好友ID
     */
    @PostMapping("/api/feed/update/friend-removed")
    void updateFeedOnFriendRemoved(@RequestParam("userId") Long userId, 
                                  @RequestParam("friendId") Long friendId);
}