package com.nexus.user.controller;

import com.nexus.user.entity.UserRelationship;
import com.nexus.user.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 好友控制器，处理好友关系相关的REST API请求
 * 包括发送好友请求、接受/拒绝好友请求、获取好友列表等操作
 */
@RestController
@RequestMapping("/api/friends")
public class FriendController {
    
    /** 好友服务实例 */
    @Autowired
    private FriendService friendService;
    
    /**
     * 发送好友请求
     * @param fromUserId 发送请求的用户ID
     * @param toUserId 接收请求的用户ID
     * @return 创建的用户关系对象
     */
    @PostMapping("/request")
    public UserRelationship sendFriendRequest(@RequestParam Long fromUserId, @RequestParam Long toUserId) {
        return friendService.sendFriendRequest(fromUserId, toUserId);
    }
    
    /**
     * 接受好友请求
     * @param fromUserId 发送请求的用户ID
     * @param toUserId 接收请求的用户ID
     * @return 更新后的用户关系对象或404错误
     */
    @PutMapping("/accept")
    public ResponseEntity<UserRelationship> acceptFriendRequest(@RequestParam Long fromUserId, @RequestParam Long toUserId) {
        UserRelationship relationship = friendService.acceptFriendRequest(fromUserId, toUserId);
        if (relationship != null) {
            return ResponseEntity.ok(relationship);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 拒绝好友请求
     * @param fromUserId 发送请求的用户ID
     * @param toUserId 接收请求的用户ID
     * @return 更新后的用户关系对象或404错误
     */
    @PutMapping("/reject")
    public ResponseEntity<UserRelationship> rejectFriendRequest(@RequestParam Long fromUserId, @RequestParam Long toUserId) {
        UserRelationship relationship = friendService.rejectFriendRequest(fromUserId, toUserId);
        if (relationship != null) {
            return ResponseEntity.ok(relationship);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 获取用户的好友列表
     * @param userId 用户ID
     * @return 好友关系列表
     */
    @GetMapping("/{userId}")
    public List<UserRelationship> getUserFriends(@PathVariable Long userId) {
        return friendService.getUserFriends(userId);
    }
    
    /**
     * 获取用户的好友ID列表
     * @param userId 用户ID
     * @return 好友ID列表
     */
    @GetMapping("/{userId}/ids")
    public List<Long> getUserFriendIds(@PathVariable Long userId) {
        return friendService.getUserFriendIds(userId);
    }
}