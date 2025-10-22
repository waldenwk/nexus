package com.nexus.user.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 用户关系实体类，映射数据库中的user_relationship表
 * 用于存储用户之间的好友关系、亲密关系等社交关系信息
 */
@Entity
@Table(name = "user_relationship")
public class UserRelationship {
    /** 关系唯一标识符 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /** 发起关系请求的用户ID */
    @Column(name = "from_user_id", nullable = false)
    private Long fromUserId;
    
    /** 接收关系请求的用户ID */
    @Column(name = "to_user_id", nullable = false)
    private Long toUserId;
    
    /** 关系状态（待确认、已接受、已屏蔽） */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RelationshipStatus status;
    
    /** 亲密关系分数，用于计算用户之间的亲密程度 */
    @Column(name = "intimacy_score")
    private Integer intimacyScore = 0;
    
    /** 关系创建时间 */
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    // Constructors
    public UserRelationship() {}
    
    public UserRelationship(Long fromUserId, Long toUserId, RelationshipStatus status) {
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getFromUserId() {
        return fromUserId;
    }
    
    public void setFromUserId(Long fromUserId) {
        this.fromUserId = fromUserId;
    }
    
    public Long getToUserId() {
        return toUserId;
    }
    
    public void setToUserId(Long toUserId) {
        this.toUserId = toUserId;
    }
    
    public RelationshipStatus getStatus() {
        return status;
    }
    
    public void setStatus(RelationshipStatus status) {
        this.status = status;
    }
    
    public Integer getIntimacyScore() {
        return intimacyScore;
    }
    
    public void setIntimacyScore(Integer intimacyScore) {
        this.intimacyScore = intimacyScore;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}