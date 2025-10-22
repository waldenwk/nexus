package com.nexus.recommendation.repository;

import com.nexus.recommendation.entity.UserPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户偏好数据访问接口
 */
@Repository
public interface UserPreferenceRepository extends JpaRepository<UserPreference, Long> {
    
    /**
     * 根据用户ID查找其所有偏好
     * @param userId 用户ID
     * @return 用户偏好列表
     */
    List<UserPreference> findByUserId(Long userId);
    
    /**
     * 根据用户ID和偏好类型查找偏好
     * @param userId 用户ID
     * @param preferenceType 偏好类型
     * @return 用户偏好列表
     */
    List<UserPreference> findByUserIdAndPreferenceType(Long userId, String preferenceType);
}