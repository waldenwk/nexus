package com.nexus.recommendation.repository;

import com.nexus.recommendation.entity.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 推荐记录数据访问接口
 */
@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
    
    /**
     * 根据用户ID查找推荐记录
     * @param userId 用户ID
     * @return 推荐记录列表
     */
    List<Recommendation> findByUserIdOrderByScoreDesc(Long userId);
    
    /**
     * 根据用户ID和推荐类型查找推荐记录
     * @param userId 用户ID
     * @param recommendationType 推荐类型
     * @return 推荐记录列表
     */
    List<Recommendation> findByUserIdAndRecommendationTypeOrderByScoreDesc(Long userId, String recommendationType);
    
    /**
     * 根据用户ID查找未查看的推荐记录
     * @param userId 用户ID
     * @return 未查看的推荐记录列表
     */
    List<Recommendation> findByUserIdAndViewedFalse(Long userId);
}