package com.nexus.recommendation.service;

import com.nexus.recommendation.entity.Recommendation;
import com.nexus.recommendation.entity.UserPreference;
import com.nexus.recommendation.repository.RecommendationRepository;
import com.nexus.recommendation.repository.UserPreferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 推荐服务实现类
 * 实现基于用户关系、偏好和行为的智能推荐算法
 */
@Service
public class RecommendationService {
    
    @Autowired
    private UserPreferenceRepository userPreferenceRepository;
    
    @Autowired
    private RecommendationRepository recommendationRepository;
    
    /**
     * 生成内容推荐
     * @param userId 用户ID
     * @return 推荐内容列表
     */
    public List<Recommendation> generateContentRecommendations(Long userId) {
        // 1. 获取用户偏好
        List<UserPreference> preferences = userPreferenceRepository.findByUserId(userId);
        
        // 2. 基于协同过滤或内容推荐算法生成推荐
        // 这里是简化实现，实际项目中会使用更复杂的算法
        // 例如基于用户相似度、内容相似度等
        
        // 3. 返回推荐结果
        return recommendationRepository.findByUserIdAndRecommendationTypeOrderByScoreDesc(userId, "CONTENT");
    }
    
    /**
     * 生成好友推荐
     * @param userId 用户ID
     * @return 推荐用户列表
     */
    public List<Recommendation> generateFriendRecommendations(Long userId) {
        // 1. 基于以下因素生成好友推荐：
        // - 相同学校和入学年份（同学）
        // - 共同好友
        // - 相似兴趣爱好
        // - 地理位置接近
        // - 工作单位相同（同事）
        
        // 2. 计算推荐分数
        // 这里是简化实现，实际项目中会使用更复杂的算法
        
        // 3. 返回推荐结果
        return recommendationRepository.findByUserIdAndRecommendationTypeOrderByScoreDesc(userId, "USER");
    }
    
    /**
     * 更新用户偏好
     * 根据用户行为动态调整偏好权重
     * @param userId 用户ID
     * @param preferenceType 偏好类型
     * @param preferenceValue 偏好值
     * @param weight 权重
     */
    public void updateUserPreference(Long userId, String preferenceType, String preferenceValue, Double weight) {
        // 查找现有偏好或创建新偏好
        List<UserPreference> existingPreferences = userPreferenceRepository
                .findByUserIdAndPreferenceType(userId, preferenceType);
        
        UserPreference preference = null;
        for (UserPreference p : existingPreferences) {
            if (p.getPreferenceValue().equals(preferenceValue)) {
                preference = p;
                break;
            }
        }
        
        if (preference == null) {
            preference = new UserPreference(userId, preferenceType, preferenceValue, weight);
        } else {
            preference.setWeight(weight);
        }
        
        userPreferenceRepository.save(preference);
    }
    
    /**
     * 标记推荐为已查看
     * @param recommendationId 推荐ID
     */
    public void markAsViewed(Long recommendationId) {
        Recommendation recommendation = recommendationRepository.findById(recommendationId).orElse(null);
        if (recommendation != null) {
            recommendation.setViewed(true);
            recommendationRepository.save(recommendation);
        }
    }
    
    /**
     * 标记推荐为已接受
     * @param recommendationId 推荐ID
     */
    public void markAsAccepted(Long recommendationId) {
        Recommendation recommendation = recommendationRepository.findById(recommendationId).orElse(null);
        if (recommendation != null) {
            recommendation.setAccepted(true);
            recommendationRepository.save(recommendation);
        }
    }
}