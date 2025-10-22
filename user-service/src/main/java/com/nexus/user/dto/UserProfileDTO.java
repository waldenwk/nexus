package com.nexus.user.dto;

import com.nexus.user.entity.ContactVisibility;
import com.nexus.user.entity.ProfileVisibility;

import java.time.LocalDateTime;

/**
 * 用户资料数据传输对象
 * 用于在API层和业务层之间传输用户资料数据
 */
public class UserProfileDTO {
    /** 用户资料唯一标识符 */
    private Long id;
    
    /** 关联的用户ID */
    private Long userId;
    
    /** 用户头像URL */
    private String avatarUrl;
    
    /** 用户个人简介 */
    private String bio;
    
    /** 用户所在城市 */
    private String city;
    
    /** 用户职业 */
    private String occupation;
    
    /** 用户兴趣爱好 */
    private String interests;
    
    /** 用户生日 */
    private LocalDateTime birthday;
    
    /** 用户性别 */
    private String gender;
    
    /** 联系方式 - 电话 */
    private String phone;
    
    /** 联系方式 - 邮箱 */
    private String contactEmail;
    
    /** 隐私设置 - 个人资料可见性 */
    private ProfileVisibility profileVisibility;
    
    /** 隐私设置 - 联系信息可见性 */
    private ContactVisibility contactVisibility;
    
    /** 资料创建时间 */
    private LocalDateTime createdAt;
    
    /** 资料更新时间 */
    private LocalDateTime updatedAt;
    
    // Constructors
    public UserProfileDTO() {}
    
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