package com.nexus.user.dto;

import java.time.LocalDateTime;

/**
 * 用户数据传输对象
 * 用于在API层和业务层之间传输用户数据
 */
public class UserDTO {
    /** 用户唯一标识符 */
    private Long id;
    
    /** 用户名 */
    private String username;
    
    /** 用户真实姓名 */
    private String realName;
    
    /** 用户所在学校 */
    private String school;
    
    /** 用户入学年份 */
    private Integer enrollmentYear;
    
    /** 用户创建时间 */
    private LocalDateTime createdAt;
    
    /** 用户信息更新时间 */
    private LocalDateTime updatedAt;
    
    // Constructors
    public UserDTO() {}
    
    public UserDTO(Long id, String username, String realName, String school, Integer enrollmentYear) {
        this.id = id;
        this.username = username;
        this.realName = realName;
        this.school = school;
        this.enrollmentYear = enrollmentYear;
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