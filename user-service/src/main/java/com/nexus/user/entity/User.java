package com.nexus.user.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 用户实体类，映射数据库中的users表
 * 包含用户的基本信息，如用户名、真实姓名、学校和入学年份等
 */
@Entity
@Table(name = "users")
public class User {
    /** 用户唯一标识符 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /** 用户名，唯一且不能为空 */
    @Column(nullable = false, unique = true)
    private String username;
    
    /** 用户真实姓名，不能为空 */
    @Column(name = "real_name", nullable = false)
    private String realName;
    
    /** 用户所在学校，不能为空 */
    @Column(nullable = false)
    private String school;
    
    /** 用户入学年份，不能为空 */
    @Column(name = "enrollment_year", nullable = false)
    private Integer enrollmentYear;
    
    /** 用户创建时间 */
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    /** 用户信息更新时间 */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructors
    public User() {}
    
    public User(String username, String realName, String school, Integer enrollmentYear) {
        this.username = username;
        this.realName = realName;
        this.school = school;
        this.enrollmentYear = enrollmentYear;
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
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getRealName() {
        return realName;
    }
    
    public void setRealName(String realName) {
        this.realName = realName;
    }
    
    public String getSchool() {
        return school;
    }
    
    public void setSchool(String school) {
        this.school = school;
    }
    
    public Integer getEnrollmentYear() {
        return enrollmentYear;
    }
    
    public void setEnrollmentYear(Integer enrollmentYear) {
        this.enrollmentYear = enrollmentYear;
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