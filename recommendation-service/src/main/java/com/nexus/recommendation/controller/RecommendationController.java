package com.nexus.recommendation.controller;

import com.nexus.recommendation.entity.Recommendation;
import com.nexus.recommendation.service.ContentRecommendationService;
import com.nexus.recommendation.service.FriendRecommendationService;
import com.nexus.recommendation.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 推荐服务控制器
 * 提供好友推荐和内容推荐相关的REST API接口
 */
@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {
    
    @Autowired
    private RecommendationService recommendationService;
    
    @Autowired
    private FriendRecommendationService friendRecommendationService;
    
    @Autowired
    private ContentRecommendationService contentRecommendationService;
    
    /**
     * 获取好友推荐
     * @param userId 用户ID
     * @return 好友推荐列表
     */
    @GetMapping("/friends/{userId}")
    public ResponseEntity<List<Recommendation>> getFriendRecommendations(@PathVariable Long userId) {
        List<Recommendation> recommendations = recommendationService.generateFriendRecommendations(userId);
        return ResponseEntity.ok(recommendations);
    }
    
    /**
     * 获取内容推荐
     * @param userId 用户ID
     * @return 内容推荐列表
     */
    @GetMapping("/content/{userId}")
    public ResponseEntity<List<Recommendation>> getContentRecommendations(@PathVariable Long userId) {
        List<Recommendation> recommendations = recommendationService.generateContentRecommendations(userId);
        return ResponseEntity.ok(recommendations);
    }
    
    /**
     * 获取综合推荐（好友+内容）
     * @param userId 用户ID
     * @return 综合推荐列表
     */
    @GetMapping("/comprehensive/{userId}")
    public ResponseEntity<List<Recommendation>> getComprehensiveRecommendations(@PathVariable Long userId) {
        // 获取好友推荐
        List<Recommendation> friendRecommendations = recommendationService.generateFriendRecommendations(userId);
        
        // 获取内容推荐
        List<Recommendation> contentRecommendations = recommendationService.generateContentRecommendations(userId);
        
        // 合并推荐结果
        friendRecommendations.addAll(contentRecommendations);
        
        return ResponseEntity.ok(friendRecommendations);
    }
    
    /**
     * 标记推荐为已查看
     * @param recommendationId 推荐ID
     * @return 操作结果
     */
    @PutMapping("/{recommendationId}/viewed")
    public ResponseEntity<String> markAsViewed(@PathVariable Long recommendationId) {
        recommendationService.markAsViewed(recommendationId);
        return ResponseEntity.ok("Recommendation marked as viewed");
    }
    
    /**
     * 标记推荐为已接受
     * @param recommendationId 推荐ID
     * @return 操作结果
     */
    @PutMapping("/{recommendationId}/accepted")
    public ResponseEntity<String> markAsAccepted(@PathVariable Long recommendationId) {
        recommendationService.markAsAccepted(recommendationId);
        return ResponseEntity.ok("Recommendation marked as accepted");
    }
}