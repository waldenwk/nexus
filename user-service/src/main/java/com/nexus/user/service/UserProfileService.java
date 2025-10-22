package com.nexus.user.service;

import com.nexus.user.entity.UserProfile;
import com.nexus.user.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 用户资料服务类，处理用户资料相关业务逻辑
 * 提供用户资料的增删改查等操作
 */
@Service
public class UserProfileService {
    
    /** 用户资料数据访问对象 */
    @Autowired
    private UserProfileRepository userProfileRepository;
    
    /**
     * 根据用户ID获取用户资料
     * @param userId 用户ID
     * @return 用户资料对象
     */
    public UserProfile getUserProfileByUserId(Long userId) {
        Optional<UserProfile> profile = userProfileRepository.findByUserId(userId);
        if (profile.isPresent()) {
            return profile.get();
        }
        
        // 如果用户资料不存在，创建一个新的
        UserProfile newProfile = new UserProfile(userId);
        return userProfileRepository.save(newProfile);
    }
    
    /**
     * 创建或更新用户资料
     * @param userProfile 用户资料对象
     * @return 保存后的用户资料对象
     */
    public UserProfile saveUserProfile(UserProfile userProfile) {
        return userProfileRepository.save(userProfile);
    }
    
    /**
     * 更新用户资料
     * @param userId 用户ID
     * @param updatedProfile 更新的用户资料对象
     * @return 更新后的用户资料对象
     */
    public UserProfile updateUserProfile(Long userId, UserProfile updatedProfile) {
        UserProfile existingProfile = getUserProfileByUserId(userId);
        
        // 更新用户资料字段
        existingProfile.setAvatarUrl(updatedProfile.getAvatarUrl());
        existingProfile.setBio(updatedProfile.getBio());
        existingProfile.setCity(updatedProfile.getCity());
        existingProfile.setOccupation(updatedProfile.getOccupation());
        existingProfile.setInterests(updatedProfile.getInterests());
        existingProfile.setBirthday(updatedProfile.getBirthday());
        existingProfile.setGender(updatedProfile.getGender());
        existingProfile.setPhone(updatedProfile.getPhone());
        existingProfile.setContactEmail(updatedProfile.getContactEmail());
        existingProfile.setProfileVisibility(updatedProfile.getProfileVisibility());
        existingProfile.setContactVisibility(updatedProfile.getContactVisibility());
        
        return userProfileRepository.save(existingProfile);
    }
    
    /**
     * 删除用户资料
     * @param userId 用户ID
     */
    public void deleteUserProfile(Long userId) {
        userProfileRepository.deleteByUserId(userId);
    }
}