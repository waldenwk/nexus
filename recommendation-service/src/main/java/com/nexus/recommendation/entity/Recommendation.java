package com.nexus.recommendation.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 推荐记录实体类
 * 存储系统向用户推荐的内容记录，用于评估推荐效果和优化算法
 */
@Entity
@Table(name = "recommendations")
public class Recommendation {
    /** 推荐记录唯一标识符 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /** 用户ID */
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    /** 被推荐内容ID */
    @Column(name = "content_id")
    private Long contentId;
    
    /** 被推荐用户ID（用于好友推荐） */
    @Column(name = "recommended_user_id")
    private Long recommendedUserId;
    
    /** 推荐类型（内容推荐、用户推荐） */
    @Column(name = "recommendation_type", nullable = false)
    private String recommendationType;
    
    /** 推荐分数 */
    @Column(name = "score", nullable = false)
    private Double score;
    
    /** 推荐理由 */
    @Column(name = "reason")
    private String reason;
    
    /** 是否已被查看 */
    @Column(name = "viewed")
    private Boolean viewed = false;
    
    /** 是否已被接受（如添加好友） */
    @Column(name = "accepted")
    private Boolean accepted = false;
    
    /** 记录创建时间 */
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    // Constructors
    public Recommendation() {}
    
    public Recommendation(Long userId, Long contentId, Long recommendedUserId, 
                         String recommendationType, Double score, String reason) {
        this.userId = userId;
        this.contentId = contentId;
        this.recommendedUserId = recommendedUserId;
        this.recommendationType = recommendationType;
        this.score = score;
        this.reason = reason;
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public Long getContentId() {
        return contentId;
    }
    
    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }
    
    public Long getRecommendedUserId() {
        return recommendedUserId;
    }
    
    public void setRecommendedUserId(Long recommendedUserId) {
        this.recommendedUserId = recommendedUserId;
    }
    
    public String getRecommendationType() {
        return recommendationType;
    }
    
    public void setRecommendationType(String recommendationType) {
        this.recommendationType = recommendationType;
    }
    
    public Double getScore() {
        return score;
    }
    
    public void setScore(Double score) {
        this.score = score;
    }
    
    public String getReason() {
        return reason;
    }
    
    public void setReason(String reason) {
        this.reason = reason;
    }
    
    public Boolean getViewed() {
        return viewed;
    }
    
    public void setViewed(Boolean viewed) {
        this.viewed = viewed;
    }
    
    public Boolean getAccepted() {
        return accepted;
    }
    
    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}