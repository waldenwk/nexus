package com.nexus.content.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 内容实体类，映射数据库中的posts表
 * 用于存储用户发布的各种类型的内容，如状态、日志、相册等
 */
@Entity
@Table(name = "posts")
public class Post {
    /** 内容唯一标识符 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /** 发布内容的用户ID */
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    /** 内容文本，使用TEXT类型存储较长文本 */
    @Column(columnDefinition = "TEXT")
    private String content;
    
    /** 内容类型（状态、日志、相册） */
    @Column(nullable = false)
    private PostType type;
    
    /** 内容创建时间 */
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    /** 内容更新时间 */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructors
    public Post() {}
    
    public Post(Long userId, String content, PostType type) {
        this.userId = userId;
        this.content = content;
        this.type = type;
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
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public PostType getType() {
        return type;
    }
    
    public void setType(PostType type) {
        this.type = type;
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