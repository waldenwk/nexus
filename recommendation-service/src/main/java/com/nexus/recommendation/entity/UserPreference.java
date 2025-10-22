package com.nexus.recommendation.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 用户偏好实体类
 * 存储用户的内容偏好、行为习惯等信息，用于个性化推荐
 */
@Entity
@Table(name = "user_preferences")
public class UserPreference {
    /** 偏好记录唯一标识符 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /** 用户ID */
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    /** 偏好类型（内容类型、标签等） */
    @Column(name = "preference_type", nullable = false)
    private String preferenceType;
    
    /** 偏好值 */
    @Column(name = "preference_value", nullable = false)
    private String preferenceValue;
    
    /** 偏好权重 */
    @Column(name = "weight", nullable = false)
    private Double weight = 0.0;
    
    /** 记录创建时间 */
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    /** 记录更新时间 */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructors
    public UserPreference() {}
    
    public UserPreference(Long userId, String preferenceType, String preferenceValue, Double weight) {
        this.userId = userId;
        this.preferenceType = preferenceType;
        this.preferenceValue = preferenceValue;
        this.weight = weight;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
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
    
    public String getPreferenceType() {
        return preferenceType;
    }
    
    public void setPreferenceType(String preferenceType) {
        this.preferenceType = preferenceType;
    }
    
    public String getPreferenceValue() {
        return preferenceValue;
    }
    
    public void setPreferenceValue(String preferenceValue) {
        this.preferenceValue = preferenceValue;
    }
    
    public Double getWeight() {
        return weight;
    }
    
    public void setWeight(Double weight) {
        this.weight = weight;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}