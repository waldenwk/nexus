package com.nexus.user.controller;

import com.nexus.user.entity.UserProfile;
import com.nexus.user.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 用户资料控制器，处理用户资料相关的REST API请求
 * 包括获取、创建、更新用户资料等操作
 */
@RestController
@RequestMapping("/api/user/profile")
public class UserProfileController {
    
    /** 用户资料服务实例 */
    @Autowired
    private UserProfileService userProfileService;
    
    /**
     * 获取用户资料
     * @param userId 用户ID
     * @return 用户资料对象
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserProfile> getUserProfile(@PathVariable Long userId) {
        UserProfile profile = userProfileService.getUserProfileByUserId(userId);
        return ResponseEntity.ok(profile);
    }
    
    /**
     * 创建或更新用户资料
     * @param userId 用户ID
     * @param userProfile 用户资料对象
     * @return 保存后的用户资料对象
     */
    @PostMapping("/{userId}")
    public ResponseEntity<UserProfile> saveUserProfile(@PathVariable Long userId, 
                                                       @RequestBody UserProfile userProfile) {
        // 确保用户ID正确
        userProfile.setUserId(userId);
        UserProfile savedProfile = userProfileService.saveUserProfile(userProfile);
        return ResponseEntity.ok(savedProfile);
    }
    
    /**
     * 更新用户资料
     * @param userId 用户ID
     * @param userProfile 更新的用户资料对象
     * @return 更新后的用户资料对象
     */
    @PutMapping("/{userId}")
    public ResponseEntity<UserProfile> updateUserProfile(@PathVariable Long userId,
                                                         @RequestBody UserProfile userProfile) {
        UserProfile updatedProfile = userProfileService.updateUserProfile(userId, userProfile);
        return ResponseEntity.ok(updatedProfile);
    }
    
    /**
     * 删除用户资料
     * @param userId 用户ID
     * @return 空响应
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUserProfile(@PathVariable Long userId) {
        userProfileService.deleteUserProfile(userId);
        return ResponseEntity.ok().build();
    }
}