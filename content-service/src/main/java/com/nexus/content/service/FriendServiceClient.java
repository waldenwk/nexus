package com.nexus.content.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 好友服务客户端接口
 * 通过Feign实现服务间调用，获取用户好友信息
 */
@FeignClient(name = "user-service")
public interface FriendServiceClient {
    
    /**
     * 获取用户的好友列表
     * @param userId 用户ID
     * @return 好友ID列表
     */
    @GetMapping("/api/friends/{userId}")
    List<Long> getUserFriends(@PathVariable("userId") Long userId);
}