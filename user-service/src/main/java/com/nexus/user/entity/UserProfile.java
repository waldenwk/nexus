package com.nexus.user.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 用户资料实体类，映射数据库中的user_profiles表
 * 包含用户的详细个人信息，如头像、个人简介、联系方式等
 */
@Entity
@Table(name = "user_profiles")
public class UserProfile {
    /** 用户资料唯一标识符 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /** 关联的用户ID */
    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;
    
    /** 用户头像URL */
    @Column(name = "avatar_url")
    private String avatarUrl;
    
    /** 用户个人简介 */
    @Column(name = "bio", length = 500)
    private String bio;
    
    /** 用户所在城市 */
    @Column(name = "city")
    private String city;
    
    /** 用户职业 */
    @Column(name = "occupation")
    private String occupation;
    
    /** 用户兴趣爱好 */
    @Column(name = "interests", length = 1000)
    private String interests;
    
    /** 用户生日 */
    @Column(name = "birthday")
    private LocalDateTime birthday;
    
    /** 用户性别 */
    @Column(name = "gender")
    private String gender;
    
    /** 联系方式 - 电话 */
    @Column(name = "phone")
    private String phone;
    
    /** 联系方式 - 邮箱 */
    @Column(name = "contact_email")
    private String contactEmail;
    
    /** 隐私设置 - 个人资料可见性 */
    @Column(name = "profile_visibility", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProfileVisibility profileVisibility = ProfileVisibility.PUBLIC;
    
    /** 隐私设置 - 联系信息可见性 */
    @Column(name = "contact_visibility", nullable = false)
    @Enumerated(EnumType.STRING)
    private ContactVisibility contactVisibility = ContactVisibility.FRIENDS_ONLY;
    
    /** 资料创建时间 */
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    /** 资料更新时间 */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructors
    public UserProfile() {}
    
    public UserProfile(Long userId) {
        this.userId = userId;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    @PrePersist
    public void prePersist() {
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
    
    public String getAvatarUrl() {
        return avatarUrl;
    }
    
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
    
    public String getBio() {
        return bio;
    }
    
    public void setBio(String bio) {
        this.bio = bio;
    }
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public String getOccupation() {
        return occupation;
    }
    
    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }
    
    public String getInterests() {
        return interests;
    }
    
    public void setInterests(String interests) {
        this.interests = interests;
    }
    
    public LocalDateTime getBirthday() {
        return birthday;
    }
    
    public void setBirthday(LocalDateTime birthday) {
        this.birthday = birthday;
    }
    
    public String getGender() {
        return gender;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getContactEmail() {
        return contactEmail;
    }
    
    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }
    
    public ProfileVisibility getProfileVisibility() {
        return profileVisibility;
    }
    
    public void setProfileVisibility(ProfileVisibility profileVisibility) {
        this.profileVisibility = profileVisibility;
    }
    
    public ContactVisibility getContactVisibility() {
        return contactVisibility;
    }
    
    public void setContactVisibility(ContactVisibility contactVisibility) {
        this.contactVisibility = contactVisibility;
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